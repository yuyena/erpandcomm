package kr.spring.stock.service;

import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;
import java.util.List;

public interface StockTransactionService {
	
	// 재고 입고 처리 (구매주문 생성 시)
	public void processStockIn(long product_num, long quantity, long emp_num, String note);
	
	// 재고 출고 처리 (판매주문 생성 시)
	public void processStockOut(long product_num, long quantity, long emp_num, String note);
	
	// 재고 입고 취소 처리 (구매주문 취소 시)
	public void cancelStockIn(long product_num, long quantity, long emp_num, String note);
	
	// 재고 출고 취소 처리 (판매주문 취소 시)
	public void cancelStockOut(long product_num, long quantity, long emp_num, String note);
	
	// 현재 재고 조회
	public CurrentStockVO getCurrentStock(long product_num);
	
	// 재고 이동 이력 조회
	public List<StockMovementVO> getStockMovementHistory(long product_num);
} 