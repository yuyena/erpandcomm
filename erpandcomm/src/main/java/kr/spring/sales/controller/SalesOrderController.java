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

import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

import kr.spring.sales.service.SalesOrderService;
import kr.spring.sales.vo.SalesOrderVO;
import kr.spring.sales.vo.SalesOrderDetailVO;
import kr.spring.product.service.ProductService;
import kr.spring.product.vo.ProductVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.client.service.ClientService;
import kr.spring.client.vo.ClientVO;

@Controller
@RequestMapping("/sales")
public class SalesOrderController {
    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ClientService clientService;

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
        // 고객만 필터링해서 전달
        List<ClientVO> customerList = clientService.getClientList().stream()
            .filter(c -> c.getClient_type() == 1)
            .collect(Collectors.toList());
        model.addAttribute("customerList", customerList);
        model.addAttribute("today", new java.util.Date());
        return "views/sales/salesOrderForm";
    }

    // 판매주문 등록 처리
    @PostMapping("/orderInsert")
    public String orderInsert(@ModelAttribute SalesOrderVO salesOrderVO) {
        // 로그인한 직원의 emp_num을 세팅
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        salesOrderVO.setEmp_num(principal.getMemberVO().getUser_num());
        if (salesOrderVO.getOrder_date() == null) {
            salesOrderVO.setOrder_date(new Date());
        }
        salesOrderService.insertSalesOrder(salesOrderVO);
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
    public String orderDelete(@RequestParam long sales_order_num) {
        salesOrderService.deleteSalesOrder(sales_order_num);
        salesOrderService.deleteSalesOrderDetail(sales_order_num);
        return "redirect:/sales/orderList";
    }
} 