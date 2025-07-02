package kr.spring.purchase.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString
public class PurchaseOrderVO {
    private long purchase_order_num;   // 구매 식별 번호
    private long supplier_num;         // 공급 업체 식별 번호
    private Date order_date;           // 주문일
    private double total_price;        // 총 구매금액
    private long emp_num;              // 구매 담당 직원
} 