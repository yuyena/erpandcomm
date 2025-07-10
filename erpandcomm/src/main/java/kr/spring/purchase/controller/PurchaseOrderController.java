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

@Controller
@RequestMapping("/purchase")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

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
    public String orderInsert(@ModelAttribute PurchaseOrderVO purchaseOrderVO) {
        // 로그인한 직원의 emp_num을 세팅
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        purchaseOrderVO.setEmp_num(principal.getMemberVO().getUser_num());
        purchaseOrderService.insertPurchaseOrder(purchaseOrderVO);
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
        purchaseOrderService.deletePurchaseOrder(purchase_order_num);
        purchaseOrderService.deletePurchaseOrderDetail(purchase_order_num);
        return "redirect:/purchase/orderList";
    }
} 