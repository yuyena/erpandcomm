package kr.spring.purchase.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class PurchaseOrderVO {
    private long purchase_order_num;   // 구매 식별 번호
    private long supplier_num;         // 공급 업체 식별 번호
    private String client_name;        // 공급처명(조인)
    private String supplier_name;      // 공급업체명(조인)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date order_date;           // 주문일
    private double total_price;        // 총 구매금액
    private long emp_num;              // 구매 담당 직원
    private String emp_name;            // 담당자명(조인)
    private List<PurchaseOrderDetailVO> details; // 주문 상세 리스트
} 