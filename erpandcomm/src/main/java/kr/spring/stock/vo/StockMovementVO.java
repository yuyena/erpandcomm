package kr.spring.stock.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString
public class StockMovementVO {
    private long movement_num;   // 재고이동 번호
    private long product_num;    // 상품 번호
    private long emp_num;        // 담당 직원
    private long movement_type;  // 이동 유형(0:입고, 1:출고)
    private long quantity;       // 이동 수량
    private Date movement_date;  // 이동 날짜
    private String note;         // 비고
} 