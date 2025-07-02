package kr.spring.sales.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString
public class SalesOrderVO {
    private long sales_order_num;   // 판매 식별 번호
    private long customer_num;      // 구매 업체 식별 번호
    private Date order_date;        // 주문일
    private double total_price;     // 총 판매금액
    private long emp_num;           // 판매 담당 직원
} 