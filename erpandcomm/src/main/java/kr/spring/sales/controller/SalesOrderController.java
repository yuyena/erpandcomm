package kr.spring.sales.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import kr.spring.sales.service.SalesOrderService;
import kr.spring.sales.vo.SalesOrderVO;
import kr.spring.sales.vo.SalesOrderDetailVO;
import kr.spring.product.service.ProductService;
import kr.spring.product.vo.ProductVO;

@Controller
@RequestMapping("/sales")
public class SalesOrderController {
    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private ProductService productService;

    // 판매주문 목록
    @GetMapping("/orderList")
    public String orderList(Model model) {
        List<SalesOrderVO> list = salesOrderService.selectSalesOrderList();
        model.addAttribute("list", list);
        return "views/sales/salesOrderList";
    }

    // 판매주문 등록 폼
    @GetMapping("/orderForm")
    public String orderForm(Model model) {
        model.addAttribute("salesOrderVO", new SalesOrderVO());
        List<ProductVO> productList = productService.selectProductList();
        model.addAttribute("productList", productList);
        return "views/sales/salesOrderForm";
    }

    // 판매주문 등록 처리
    @PostMapping("/orderInsert")
    public String orderInsert(@ModelAttribute SalesOrderVO salesOrderVO) {
        // 1. 주문 마스터 저장
        salesOrderService.insertSalesOrder(salesOrderVO);
        // 2. 주문 상세(여러 상품) 저장
        if (salesOrderVO.getDetails() != null) {
            for (SalesOrderDetailVO detail : salesOrderVO.getDetails()) {
                detail.setSales_order_num(salesOrderVO.getSales_order_num());
                salesOrderService.insertSalesOrderDetail(detail);
            }
        }
        return "redirect:/sales/orderList";
    }

    // 판매주문 상세
    @GetMapping("/orderDetail")
    public String orderDetail(@RequestParam long sales_order_num, Model model) {
        SalesOrderVO order = salesOrderService.selectSalesOrder(sales_order_num);
        List<SalesOrderDetailVO> details = salesOrderService.selectSalesOrderDetailList(sales_order_num);
        model.addAttribute("order", order);
        model.addAttribute("details", details);
        return "views/sales/salesOrderDetail";
    }

    // 판매주문 수정 폼
    @GetMapping("/orderEdit")
    public String orderEdit(@RequestParam long sales_order_num, Model model) {
        SalesOrderVO order = salesOrderService.selectSalesOrder(sales_order_num);
        model.addAttribute("salesOrderVO", order);
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
    public String orderDelete(@RequestParam long sales_order_num) {
        salesOrderService.deleteSalesOrder(sales_order_num);
        salesOrderService.deleteSalesOrderDetail(sales_order_num);
        return "redirect:/sales/orderList";
    }
} 