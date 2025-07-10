package kr.spring.purchase.controller;

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

import java.util.List;
import java.util.stream.Collectors;

import kr.spring.purchase.service.PurchaseOrderService;
import kr.spring.purchase.vo.PurchaseOrderVO;
import kr.spring.purchase.vo.PurchaseOrderDetailVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.client.service.ClientService;
import kr.spring.client.vo.ClientVO;
import kr.spring.product.service.ProductService;
import kr.spring.product.vo.ProductVO;
import kr.spring.stock.service.StockTransactionService;

@Controller
@RequestMapping("/purchase")
public class PurchaseOrderController {
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
        List<PurchaseOrderVO> list = purchaseOrderService.selectPurchaseOrderList();
        model.addAttribute("list", list);
        return "views/purchase/purchaseOrderList";
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
        return "views/purchase/purchaseOrderForm";
    }

    // 구매주문 등록 처리
    @PostMapping("/orderInsert")
    public String orderInsert(@ModelAttribute PurchaseOrderVO purchaseOrderVO, RedirectAttributes redirectAttributes) {
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
                    System.err.println("재고 입고 처리 실패: " + e.getMessage());
                }
            }
        }
        
        redirectAttributes.addFlashAttribute("orderSuccess", true);
        return "redirect:/purchase/orderList";
    }

    // 구매주문 상세
    @GetMapping("/orderDetail")
    public String orderDetail(@RequestParam long purchase_order_num, Model model) {
        PurchaseOrderVO order = purchaseOrderService.selectPurchaseOrder(purchase_order_num);
        List<PurchaseOrderDetailVO> details = purchaseOrderService.selectPurchaseOrderDetailList(purchase_order_num);
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        return "views/purchase/purchaseOrderDetail";
    }

    // 구매주문 수정 폼
    @GetMapping("/orderEdit")
    public String orderEdit(@RequestParam long purchase_order_num, Model model) {
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
    public String orderDelete(@RequestParam long purchase_order_num) {
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
} 