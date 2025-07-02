package kr.spring.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import kr.spring.purchase.service.PurchaseOrderService;
import kr.spring.purchase.vo.PurchaseOrderVO;
import kr.spring.purchase.vo.PurchaseOrderDetailVO;

@Controller
@RequestMapping("/purchase")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

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
        return "views/purchase/purchaseOrderForm";
    }

    // 구매주문 등록 처리
    @PostMapping("/orderInsert")
    public String orderInsert(@ModelAttribute PurchaseOrderVO purchaseOrderVO) {
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