package kr.spring.stock.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;

@Mapper
public interface StockTransactionMapper {
	
	// 현재 재고 조회
	public CurrentStockVO selectCurrentStock(long product_num);
	
	// 재고 이동 등록
	public void insertStockMovement(StockMovementVO stockMovement);
	
	// 현재 재고 업데이트 (입고/출고 시)
	public void updateCurrentStock(Map<String, Object> map);
	
	// 재고 이동 이력 조회
	public List<StockMovementVO> selectStockMovementList(long product_num);
	
	// 재고 정보가 없을 경우 새로 생성
	public void insertCurrentStockIfNotExists(Map<String, Object> map);
} 