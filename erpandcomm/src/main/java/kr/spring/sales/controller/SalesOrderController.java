package kr.spring.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;

import kr.spring.sales.service.SalesOrderService;
import kr.spring.sales.vo.SalesOrderVO;
import kr.spring.sales.vo.SalesOrderDetailVO;
import kr.spring.product.service.ProductService;
import kr.spring.product.vo.ProductVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.client.service.ClientService;
import kr.spring.client.vo.ClientVO;
import kr.spring.stock.service.StockTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/sales")
public class SalesOrderController {
    private static final Logger logger = LoggerFactory.getLogger(SalesOrderController.class);
    
    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;
    
    @Autowired
    private StockTransactionService stockTransactionService;

    // 판매주문 목록
    @GetMapping("/orderList")
    public String orderList(Model model) {
        try {
            List<SalesOrderVO> list = salesOrderService.selectSalesOrderList();
            model.addAttribute("list", list);
            return "views/sales/salesOrderList";
        } catch (Exception e) {
            logger.error("판매주문 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "주문 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("list", new java.util.ArrayList<>());
            return "views/sales/salesOrderList";
        }
    }

    // 판매주문 등록 폼
    @GetMapping("/orderForm")
    public String orderForm(Model model) {
        model.addAttribute("salesOrderVO", new SalesOrderVO());
        List<ProductVO> productList = productService.selectProductList();
        // 카테고리별로 그룹핑
        Map<String, List<ProductVO>> productCategoryMap = new LinkedHashMap<>();
        for (ProductVO product : productList) {
            String category = product.getCategory_name();
            if (!productCategoryMap.containsKey(category)) {
                productCategoryMap.put(category, new java.util.ArrayList<>());
            }
            productCategoryMap.get(category).add(product);
        }
        model.addAttribute("productCategoryMap", productCategoryMap);
        // 고객만 필터링해서 전달
        List<ClientVO> customerList = clientService.getClientList().stream()
            .filter(c -> c.getClient_type() == 1)
            .collect(Collectors.toList());
        model.addAttribute("customerList", customerList);
        model.addAttribute("today", new java.util.Date());
        return "views/sales/salesOrderForm";
    }

    // 판매주문 완료 페이지
    @GetMapping("/orderComplete")
    public String orderComplete(@RequestParam(name = "sales_order_num") long sales_order_num, Model model) {
        try {
            SalesOrderVO order = salesOrderService.selectSalesOrder(sales_order_num);
            List<SalesOrderDetailVO> details = salesOrderService.selectSalesOrderDetailList(sales_order_num);
            
            if (order == null) {
                return "redirect:/sales/orderList";
            }
            
            model.addAttribute("order", order);
            model.addAttribute("details", details);
            return "views/sales/salesOrderComplete";
        } catch (Exception e) {
            logger.error("판매주문 완료 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/sales/orderList";
        }
    }

    // 판매주문 등록 처리
    @PostMapping("/orderInsert")
    public String orderInsert(@ModelAttribute SalesOrderVO salesOrderVO, RedirectAttributes redirectAttributes) {
        try {
            // 수정인지 새로 등록인지 확인
            boolean isUpdate = salesOrderVO.getSales_order_num() != 0;
            
            if (isUpdate) {
                // 수정 처리
                salesOrderService.updateSalesOrder(salesOrderVO);
                logger.info("판매주문 수정 완료: 주문번호={}", salesOrderVO.getSales_order_num());
                redirectAttributes.addFlashAttribute("orderSuccess", true);
                return "redirect:/sales/orderList";
            }
            
            // 필수 필드 검증 (새로 등록인 경우만)
            if (salesOrderVO.getCustomer_num() == 0) {
                redirectAttributes.addFlashAttribute("orderError", "고객을 선택해주세요.");
                return "redirect:/sales/orderForm";
            }
            
            if (salesOrderVO.getDetails() == null || salesOrderVO.getDetails().isEmpty()) {
                redirectAttributes.addFlashAttribute("orderError", "주문 상세를 추가해주세요.");
                return "redirect:/sales/orderForm";
            }
            
            // 로그인한 직원의 emp_num을 세팅
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            
            // PrincipalDetails 타입인지 확인 후 안전하게 처리
            if (principal instanceof PrincipalDetails) {
                salesOrderVO.setEmp_num(((PrincipalDetails) principal).getMemberVO().getUser_num());
            } else {
                // PrincipalDetails가 아닌 경우 기본값 설정 (임시 처리)
                salesOrderVO.setEmp_num(1L); // 기본 직원 번호
            }
            
            if (salesOrderVO.getOrder_date() == null) {
                salesOrderVO.setOrder_date(new Date());
            }
            
            logger.info("판매주문 생성 시작: 고객번호={}, 총금액={}", salesOrderVO.getCustomer_num(), salesOrderVO.getTotal_price());
            
            // 1. 판매주문 등록
            salesOrderService.insertSalesOrder(salesOrderVO);
            
            // 2. 판매주문 상세 등록 및 재고 출고 처리
            if (salesOrderVO.getDetails() != null) {
                for (SalesOrderDetailVO detail : salesOrderVO.getDetails()) {
                    detail.setSales_order_num(salesOrderVO.getSales_order_num());
                    salesOrderService.insertSalesOrderDetail(detail);
                    
                    // 재고 출고 처리
                    try {
                        stockTransactionService.processStockOut(
                            detail.getProduct_num(), 
                            detail.getQuantity(), 
                            salesOrderVO.getEmp_num(),
                            "판매주문 #" + salesOrderVO.getSales_order_num() + " 출고"
                        );
                    } catch (Exception e) {
                        // 재고 처리 실패 시 예외 처리 (실제로는 더 정교한 예외 처리 필요)
                        logger.error("재고 출고 처리 실패: {}", e.getMessage(), e);
                        throw new RuntimeException("재고 처리 중 오류가 발생했습니다: " + e.getMessage());
                    }
                }
            }
            
            logger.info("판매주문 생성 완료: 주문번호={}", salesOrderVO.getSales_order_num());
            // 완료 페이지로 리다이렉트
            return "redirect:/sales/orderComplete?sales_order_num=" + salesOrderVO.getSales_order_num();
            
        } catch (Exception e) {
            logger.error("판매주문 생성 중 오류 발생: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("orderError", "주문 생성 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/sales/orderForm";
        }
    }

    // 판매주문 상세
    @GetMapping("/orderDetail")
    public String orderDetail(@RequestParam(name = "sales_order_num") long sales_order_num, Model model) {
        SalesOrderVO order = salesOrderService.selectSalesOrder(sales_order_num);
        List<SalesOrderDetailVO> details = salesOrderService.selectSalesOrderDetailList(sales_order_num);
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        return "views/sales/salesOrderDetail";
    }

    // 판매주문 수정 폼
    @GetMapping("/orderEdit")
    public String orderEdit(@RequestParam(name = "sales_order_num") long sales_order_num, Model model) {
        SalesOrderVO order = salesOrderService.selectSalesOrder(sales_order_num);
        model.addAttribute("salesOrderVO", order);
        
        // 고객만 필터링해서 전달
        List<ClientVO> customerList = clientService.getClientList().stream()
            .filter(c -> c.getClient_type() == 1)
            .collect(Collectors.toList());
        model.addAttribute("customerList", customerList);
        
        // 상품 목록도 전달
        List<ProductVO> productList = productService.selectProductList();
        model.addAttribute("productList", productList);
        return "views/sales/salesOrderForm";
    }

    // 판매주문 수정 처리
    @PostMapping("/orderUpdate")
    public String orderUpdate(@ModelAttribute SalesOrderVO salesOrderVO) {
        salesOrderService.updateSalesOrder(salesOrderVO);
        return "redirect:/sales/orderList";
    }

    // 판매주문 삭제
    @GetMapping("/orderDelete")
    public String orderDelete(@RequestParam(name = "sales_order_num") long sales_order_num) {
        // 1. 기존 주문 상세 조회 (재고 취소를 위해)
        List<SalesOrderDetailVO> details = salesOrderService.selectSalesOrderDetailList(sales_order_num);
        SalesOrderVO order = salesOrderService.selectSalesOrder(sales_order_num);
        
        // 2. 재고 출고 취소 처리
        if (details != null) {
            for (SalesOrderDetailVO detail : details) {
                try {
                    stockTransactionService.cancelStockOut(
                        detail.getProduct_num(), 
                        detail.getQuantity(), 
                        order.getEmp_num(),
                        "판매주문 #" + sales_order_num + " 취소"
                    );
                } catch (Exception e) {
                    // 재고 처리 실패 시 예외 처리
                    System.err.println("재고 출고 취소 처리 실패: " + e.getMessage());
                }
            }
        }
        
        // 3. 주문 삭제
        salesOrderService.deleteSalesOrder(sales_order_num);
        salesOrderService.deleteSalesOrderDetail(sales_order_num);
        
        return "redirect:/sales/orderList";
    }
} 