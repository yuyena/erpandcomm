package kr.spring.product.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.member.vo.MemberVO;
import kr.spring.product.vo.ProductVO;

@Mapper
public interface ProductMapper {
	
	public List<ProductVO> selectList(Map<String, Object> map);
	public Integer selectRowCount(Map<String, Object> map);
	public ProductVO selectProduct(Long product_num);
	public List<Map<String, Object>> selectCategoryList();

	// 상품 전체 목록 조회
	List<ProductVO> selectProductList();

}













































