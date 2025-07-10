package kr.spring.sales.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class SalesOrderVO {
    private long sales_order_num;   // 판매 식별 번호
    private long customer_num;      // 구매 업체 식별 번호
    private String client_name;     // 고객명(조인)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date order_date;        // 주문일
    private double total_price;     // 총 판매금액
    private long emp_num;           // 판매 담당 직원
    private List<SalesOrderDetailVO> details; // 주문 상세 리스트
} 