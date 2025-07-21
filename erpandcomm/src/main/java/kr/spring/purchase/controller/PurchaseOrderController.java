package kr.spring.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import kr.spring.purchase.service.PurchaseOrderService;
import kr.spring.purchase.vo.PurchaseOrderVO;
import kr.spring.purchase.vo.PurchaseOrderDetailVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.client.service.ClientService;
import kr.spring.client.vo.ClientVO;
import kr.spring.product.service.ProductService;
import kr.spring.product.vo.ProductVO;
import kr.spring.stock.service.StockTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/purchase")
public class PurchaseOrderController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderController.class);
    
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private StockTransactionService stockTransactionService;

    // 구매주문 목록
    @GetMapping("/orderList")
    public String orderList(Model model) {
        try {
            List<PurchaseOrderVO> list = purchaseOrderService.selectPurchaseOrderList();
            model.addAttribute("list", list);
            return "views/purchase/purchaseOrderList";
        } catch (Exception e) {
            logger.error("구매주문 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("error", "주문 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("list", new java.util.ArrayList<>());
            return "views/purchase/purchaseOrderList";
        }
    }

    // 구매주문 등록 폼
    @GetMapping("/orderForm")
    public String orderForm(Model model) {
        model.addAttribute("purchaseOrderVO", new PurchaseOrderVO());
        // 공급업체만 필터링해서 전달
        List<ClientVO> supplierList = clientService.getClientList().stream()
            .filter(c -> c.getClient_type() == 0)
            .collect(Collectors.toList());
        model.addAttribute("supplierList", supplierList);
        // 상품 목록도 전달
        List<ProductVO> productList = productService.selectProductList();
        model.addAttribute("productList", productList);
        // 로그인 사원 이름 추가
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String empName = "";
        if (principal instanceof PrincipalDetails) {
            empName = ((PrincipalDetails) principal).getMemberVO().getUser_name();
        }
        model.addAttribute("empName", empName);
        // 주문일(today) 추가
        model.addAttribute("today", new java.util.Date());
        return "views/purchase/purchaseOrderForm";
    }

    // 구매주문 완료 페이지
    @GetMapping("/orderComplete")
    public String orderComplete(@RequestParam(name = "purchase_order_num") long purchase_order_num, Model model) {
        try {
            PurchaseOrderVO order = purchaseOrderService.selectPurchaseOrder(purchase_order_num);
            List<PurchaseOrderDetailVO> details = purchaseOrderService.selectPurchaseOrderDetailList(purchase_order_num);
            
            if (order == null) {
                return "redirect:/purchase/orderList";
            }
            
            model.addAttribute("order", order);
            model.addAttribute("details", details);
            return "views/purchase/purchaseOrderComplete";
        } catch (Exception e) {
            logger.error("구매주문 완료 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/purchase/orderList";
        }
    }

    // 구매주문 등록 처리
    @PostMapping("/orderInsert")
    public String orderInsert(@ModelAttribute PurchaseOrderVO purchaseOrderVO, RedirectAttributes redirectAttributes) {
        try {
            // 수정인지 새로 등록인지 확인
            boolean isUpdate = purchaseOrderVO.getPurchase_order_num() != 0;
            
            if (isUpdate) {
                // 수정 처리
                purchaseOrderService.updatePurchaseOrder(purchaseOrderVO);
                logger.info("구매주문 수정 완료: 주문번호={}", purchaseOrderVO.getPurchase_order_num());
                redirectAttributes.addFlashAttribute("orderSuccess", true);
                return "redirect:/purchase/orderList";
            }
            
            // 필수 필드 검증 (새로 등록인 경우만)
            if (purchaseOrderVO.getSupplier_num() == 0) {
                redirectAttributes.addFlashAttribute("orderError", "공급업체를 선택해주세요.");
                return "redirect:/purchase/orderForm";
            }
            
            if (purchaseOrderVO.getDetails() == null || purchaseOrderVO.getDetails().isEmpty()) {
                redirectAttributes.addFlashAttribute("orderError", "주문 상세를 추가해주세요.");
                return "redirect:/purchase/orderForm";
            }
            
            // 로그인한 직원의 emp_num을 세팅
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            
            // PrincipalDetails 타입인지 확인 후 안전하게 처리
            if (principal instanceof PrincipalDetails) {
                purchaseOrderVO.setEmp_num(((PrincipalDetails) principal).getMemberVO().getUser_num());
            } else {
                // PrincipalDetails가 아닌 경우 기본값 설정 (임시 처리)
                purchaseOrderVO.setEmp_num(1L); // 기본 직원 번호
            }
            
            logger.info("구매주문 생성 시작: 공급업체번호={}, 총금액={}", purchaseOrderVO.getSupplier_num(), purchaseOrderVO.getTotal_price());
            
            // 1. 구매주문 등록
            purchaseOrderService.insertPurchaseOrder(purchaseOrderVO);
            
            // 2. 구매주문 상세 등록 및 재고 입고 처리
            if (purchaseOrderVO.getDetails() != null) {
                for (PurchaseOrderDetailVO detail : purchaseOrderVO.getDetails()) {
                    detail.setPurchase_order_num(purchaseOrderVO.getPurchase_order_num());
                    purchaseOrderService.insertPurchaseOrderDetail(detail);
                    
                    // 재고 입고 처리
                    try {
                        stockTransactionService.processStockIn(
                            detail.getProduct_num(), 
                            detail.getQuantity(), 
                            purchaseOrderVO.getEmp_num(),
                            "구매주문 #" + purchaseOrderVO.getPurchase_order_num() + " 입고"
                        );
                    } catch (Exception e) {
                        // 재고 처리 실패 시 예외 처리 (실제로는 더 정교한 예외 처리 필요)
                        logger.error("재고 입고 처리 실패: {}", e.getMessage(), e);
                        throw new RuntimeException("재고 처리 중 오류가 발생했습니다: " + e.getMessage());
                    }
                }
            }
            
            logger.info("구매주문 생성 완료: 주문번호={}", purchaseOrderVO.getPurchase_order_num());
            // 완료 페이지로 리다이렉트
            return "redirect:/purchase/orderComplete?purchase_order_num=" + purchaseOrderVO.getPurchase_order_num();
            
        } catch (Exception e) {
            logger.error("구매주문 생성 중 오류 발생: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("orderError", "주문 생성 중 오류가 발생했습니다: " + e.getMessage());
            return "redirect:/purchase/orderForm";
        }
    }

    // 구매주문 상세
    @GetMapping("/orderDetail")
    public String orderDetail(@RequestParam(name = "purchase_order_num") long purchase_order_num, Model model) {
        PurchaseOrderVO order = purchaseOrderService.selectPurchaseOrder(purchase_order_num);
        List<PurchaseOrderDetailVO> details = purchaseOrderService.selectPurchaseOrderDetailList(purchase_order_num);
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        return "views/purchase/purchaseOrderDetail";
    }

    // 구매주문 수정 폼
    @GetMapping("/orderEdit")
    public String orderEdit(@RequestParam(name = "purchase_order_num") long purchase_order_num, Model model) {
        PurchaseOrderVO order = purchaseOrderService.selectPurchaseOrder(purchase_order_num);
        model.addAttribute("purchaseOrderVO", order);
        
        // 공급업체만 필터링해서 전달
        List<ClientVO> supplierList = clientService.getClientList().stream()
            .filter(c -> c.getClient_type() == 0)
            .collect(Collectors.toList());
        model.addAttribute("supplierList", supplierList);
        
        // 상품 목록도 전달
        List<ProductVO> productList = productService.selectProductList();
        model.addAttribute("productList", productList);
        return "views/purchase/purchaseOrderForm";
    }

    // 구매주문 수정 처리
    @PostMapping("/orderUpdate")
    public String orderUpdate(@ModelAttribute PurchaseOrderVO purchaseOrderVO) {
        purchaseOrderService.updatePurchaseOrder(purchaseOrderVO);
        return "redirect:/purchase/orderList";
    }

    // 구매주문 삭제
    @GetMapping("/orderDelete")
    public String orderDelete(@RequestParam(name = "purchase_order_num") long purchase_order_num) {
        // 1. 기존 주문 상세 조회 (재고 취소를 위해)
        List<PurchaseOrderDetailVO> details = purchaseOrderService.selectPurchaseOrderDetailList(purchase_order_num);
        PurchaseOrderVO order = purchaseOrderService.selectPurchaseOrder(purchase_order_num);
        
        // 2. 재고 입고 취소 처리
        if (details != null) {
            for (PurchaseOrderDetailVO detail : details) {
                try {
                    stockTransactionService.cancelStockIn(
                        detail.getProduct_num(), 
                        detail.getQuantity(), 
                        order.getEmp_num(),
                        "구매주문 #" + purchase_order_num + " 취소"
                    );
                } catch (Exception e) {
                    // 재고 처리 실패 시 예외 처리
                    System.err.println("재고 입고 취소 처리 실패: " + e.getMessage());
                }
            }
        }
        
        // 3. 주문 삭제
        purchaseOrderService.deletePurchaseOrder(purchase_order_num);
        purchaseOrderService.deletePurchaseOrderDetail(purchase_order_num);
        
        return "redirect:/purchase/orderList";
    }

    @GetMapping("/stats/monthly")
    @ResponseBody
    public List<Map<String, Object>> getMonthlyStats() {
        return purchaseOrderService.getMonthlyPurchaseStats();
    }
} 