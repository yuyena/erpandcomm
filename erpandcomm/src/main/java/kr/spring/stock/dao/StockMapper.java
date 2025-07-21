package kr.spring.stock.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.product.vo.ProductVO;
import kr.spring.stock.vo.StockMovementVO;
@Mapper
public interface StockMapper {
	
	// 특정 상품의 재고 히스토리 조회
	public StockMovementVO selectStockMovementHistory(long product_num);

	 
   // 재고 전체/검색 레코드 수
   public Integer selectRowCountStock(Map<String, Object> map);

   // 재고 전체/검색 목록 (페이징 포함)
   public List<ProductVO> selectStockList(Map<String, Object> map);

   
   
   
   // 재고 수량 업데이트
   public void updateStock(Map<String, Object> map);

   // 재고 정보 추가 (current_stock 테이블에 없는 경우)
   public void insertStock(Map<String, Object> map);

   // 상품의 최소/최대 재고 수정
   public void updateMinMaxStock(Map<String, Object> map);
   // 재고 상태별 개수 조회 (대시보드용)
   public Map<String, Object> selectStockStatusCount();

} 