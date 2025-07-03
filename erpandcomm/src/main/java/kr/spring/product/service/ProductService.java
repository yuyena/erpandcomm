package kr.spring.product.service;

import java.util.List;
import java.util.Map;

import kr.spring.member.vo.MemberVO;
import kr.spring.product.vo.ProductVO;

public interface ProductService {
	
	public List<ProductVO> selectList(Map<String, Object> map);
	public Integer selectRowCount(Map<String, Object> map);
	public ProductVO selectProduct(Long product_num);
	public List<Map<String, Object>> selectCategoryList();

	// 상품 전체 목록 조회
	List<ProductVO> selectProductList();

}
