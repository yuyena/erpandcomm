package kr.spring.product.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductVO {
	private long product_num;
	private long category_num;
	private String category_name;
	private int status;
	private double unit_price;
	private String unit;
}
