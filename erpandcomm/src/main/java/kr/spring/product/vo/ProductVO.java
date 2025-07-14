package kr.spring.product.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductVO {
	private Long product_num;
    private Long category_num;
    private String category_name; 
    private String product_name; 
    private Integer status;
    private Double unit_price;
    private String unit;
    private Date product_date;   
    
    private Integer current_quantity;
    private Long min_stock;          // 최소재고
    private Long max_stock;          // 최대재고
    private Date last_in_date;       // 마지막 입고일
    private Date last_out_date;      // 마지막 출고일
    private Long last_movement_num;  // 마지막 재고이동번호
    private Integer version_num;        // 동시성 제어용 버전
    private Date updated_date;       // 마지막 거래/수정일
    private String stock_status;
}