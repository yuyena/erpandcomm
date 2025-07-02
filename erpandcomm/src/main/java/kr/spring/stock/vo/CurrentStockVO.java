package kr.spring.stock.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString
public class CurrentStockVO {
    private long product_num;        // 상품번호
    private long current_quantity;   // 현재재고수량
    private long min_stock;          // 최소재고
    private long max_stock;          // 최대재고
    private Date last_in_date;       // 마지막 입고일
    private Date last_out_date;      // 마지막 출고일
    private long last_movement_num;  // 마지막 재고이동번호
    private long version_num;        // 동시성 제어용 버전
    private Date updated_date;       // 마지막 거래/수정일
} 