package kr.spring.sales.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalesOrderDetailVO {
    private long sales_order_num;   // 판매 식별 번호 (PK, FK)
    private long product_num;       // 상품 식별 번호 (FK)
    private int quantity;           // 주문 수량
    private double unit_price;      // 판매 단가
    private String product_name;    // 상품명
} 