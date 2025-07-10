package kr.spring.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.stock.dao.StockTransactionMapper;
import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
public class StockTransactionServiceImpl implements StockTransactionService {

	@Autowired
	private StockTransactionMapper stockTransactionMapper;
	
	@Override
	@Transactional
	public void processStockIn(long product_num, long quantity, long emp_num, String note) {
		// 1. 현재 재고 조회
		CurrentStockVO currentStock = stockTransactionMapper.selectCurrentStock(product_num);
		
		// 2. 재고 정보가 없으면 새로 생성
		if (currentStock == null) {
			Map<String, Object> insertMap = Map.of("product_num", product_num);
			stockTransactionMapper.insertCurrentStockIfNotExists(insertMap);
			currentStock = stockTransactionMapper.selectCurrentStock(product_num);
		}
		
		// 3. 재고 이동 이력 등록
		StockMovementVO movement = new StockMovementVO();
		movement.setProduct_num(product_num);
		movement.setEmp_num(emp_num);
		movement.setMovement_type(0); // 0: 입고
		movement.setQuantity(quantity);
		movement.setMovement_date(new Date());
		movement.setNote(note != null ? note : "구매주문 입고");
		
		stockTransactionMapper.insertStockMovement(movement);
		
		// 4. 현재 재고 업데이트
		long newQuantity = currentStock.getCurrent_quantity() + quantity;
		Map<String, Object> updateMap = Map.of(
			"product_num", product_num,
			"current_quantity", newQuantity,
			"movement_type", 0
		);
		stockTransactionMapper.updateCurrentStock(updateMap);
	}
	
	@Override
	@Transactional
	public void processStockOut(long product_num, long quantity, long emp_num, String note) {
		// 1. 현재 재고 조회
		CurrentStockVO currentStock = stockTransactionMapper.selectCurrentStock(product_num);
		
		// 2. 재고 부족 체크
		if (currentStock == null || currentStock.getCurrent_quantity() < quantity) {
			throw new RuntimeException("재고가 부족합니다. 현재재고: " + 
				(currentStock != null ? currentStock.getCurrent_quantity() : 0) + 
				", 요청수량: " + quantity);
		}
		
		// 3. 재고 이동 이력 등록
		StockMovementVO movement = new StockMovementVO();
		movement.setProduct_num(product_num);
		movement.setEmp_num(emp_num);
		movement.setMovement_type(1); // 1: 출고
		movement.setQuantity(quantity);
		movement.setMovement_date(new Date());
		movement.setNote(note != null ? note : "판매주문 출고");
		
		stockTransactionMapper.insertStockMovement(movement);
		
		// 4. 현재 재고 업데이트
		long newQuantity = currentStock.getCurrent_quantity() - quantity;
		Map<String, Object> updateMap = Map.of(
			"product_num", product_num,
			"current_quantity", newQuantity,
			"movement_type", 1
		);
		stockTransactionMapper.updateCurrentStock(updateMap);
	}
	
	@Override
	@Transactional
	public void cancelStockIn(long product_num, long quantity, long emp_num, String note) {
		// 입고 취소 = 출고 처리
		processStockOut(product_num, quantity, emp_num, note != null ? note : "구매주문 취소");
	}
	
	@Override
	@Transactional
	public void cancelStockOut(long product_num, long quantity, long emp_num, String note) {
		// 출고 취소 = 입고 처리
		processStockIn(product_num, quantity, emp_num, note != null ? note : "판매주문 취소");
	}
	
	@Override
	public CurrentStockVO getCurrentStock(long product_num) {
		return stockTransactionMapper.selectCurrentStock(product_num);
	}
	
	@Override
	public List<StockMovementVO> getStockMovementHistory(long product_num) {
		return stockTransactionMapper.selectStockMovementList(product_num);
	}
} 