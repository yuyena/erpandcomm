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
import kr.spring.stock.vo.CurrentStockVO;

@Mapper
public interface ProductMapper {
	//전체 상품 조회/검색
	public List<ProductVO> selectList(Map<String, Object> map);
	//상품 갯수
	public Integer selectRowCount(Map<String, Object> map);
	//상품 상세
	public ProductVO selectProduct(Long product_num);
	//상품 카테고리
	public List<Map<String, Object>> selectCategoryList();
	//상품 등록
	@Select("SELECT product_seq.nextval FROM dual")
    public Long selectProductNum();
    @Insert("INSERT INTO product (product_num, category_num, product_name, status, unit_price, unit) VALUES ( #{product_num}, #{category_num}, #{product_name}, #{status}, #{unit_price}, #{unit})")
    public void insertProduct(ProductVO product);
    @Insert("INSERT INTO current_stock (product_num, current_quantity,min_stock,max_stock) VALUES (#{product_num}, #{current_quantity}, #{min_stock},#{max_stock})")
    public void insertCurrentStock(ProductVO stock);
	//상품 수정
    public void updateProduct(ProductVO product); 
    //상품 삭제
    
	
	
	
	
	// 상품 전체 목록 조회
	List<ProductVO> selectProductList();

}













































