package kr.spring.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.spring.stock.dao.StockTransactionMapper;
import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
public class StockTransactionServiceImpl implements StockTransactionService {

	private static final Logger logger = LoggerFactory.getLogger(StockTransactionServiceImpl.class);
	
	@Autowired
	private StockTransactionMapper stockTransactionMapper;
	
	@Override
	@Transactional
	public void processStockIn(long product_num, long quantity, long emp_num, String note) {
		try {
			logger.info("재고 입고 처리 시작: 상품번호={}, 수량={}, 직원번호={}", product_num, quantity, emp_num);
			
			// 1. 현재 재고 조회
			CurrentStockVO currentStock = stockTransactionMapper.selectCurrentStock(product_num);
			logger.debug("현재 재고 조회 결과: {}", currentStock);
			
			// 2. 재고 정보가 없으면 새로 생성
			if (currentStock == null) {
				logger.info("재고 정보가 없어 새로 생성: 상품번호={}", product_num);
				Map<String, Object> insertMap = Map.of("product_num", product_num);
				stockTransactionMapper.insertCurrentStockIfNotExists(insertMap);
				currentStock = stockTransactionMapper.selectCurrentStock(product_num);
				logger.debug("새로 생성된 재고 정보: {}", currentStock);
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
			logger.debug("재고 이동 이력 등록 완료: {}", movement);
			
			// 4. 현재 재고 업데이트
			long newQuantity = currentStock.getCurrent_quantity() + quantity;
			Map<String, Object> updateMap = Map.of(
				"product_num", product_num,
				"current_quantity", newQuantity,
				"movement_type", 0
			);
			stockTransactionMapper.updateCurrentStock(updateMap);
			logger.info("재고 입고 처리 완료: 상품번호={}, 기존수량={}, 입고수량={}, 새로운수량={}", 
				product_num, currentStock.getCurrent_quantity(), quantity, newQuantity);
			
		} catch (Exception e) {
			logger.error("재고 입고 처리 중 오류 발생: 상품번호={}, 수량={}, 오류={}", product_num, quantity, e.getMessage(), e);
			throw new RuntimeException("재고 입고 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}
	
	@Override
	@Transactional
	public void processStockOut(long product_num, long quantity, long emp_num, String note) {
		try {
			logger.info("재고 출고 처리 시작: 상품번호={}, 수량={}, 직원번호={}", product_num, quantity, emp_num);
			
			// 1. 현재 재고 조회
			CurrentStockVO currentStock = stockTransactionMapper.selectCurrentStock(product_num);
			logger.debug("현재 재고 조회 결과: {}", currentStock);
			
			// 2. 재고 부족 체크
			if (currentStock == null || currentStock.getCurrent_quantity() < quantity) {
				String errorMsg = "재고가 부족합니다. 현재재고: " + 
					(currentStock != null ? currentStock.getCurrent_quantity() : 0) + 
					", 요청수량: " + quantity;
				logger.error(errorMsg);
				throw new RuntimeException(errorMsg);
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
			logger.debug("재고 이동 이력 등록 완료: {}", movement);
			
			// 4. 현재 재고 업데이트
			long newQuantity = currentStock.getCurrent_quantity() - quantity;
			Map<String, Object> updateMap = Map.of(
				"product_num", product_num,
				"current_quantity", newQuantity,
				"movement_type", 1
			);
			stockTransactionMapper.updateCurrentStock(updateMap);
			logger.info("재고 출고 처리 완료: 상품번호={}, 기존수량={}, 출고수량={}, 새로운수량={}", 
				product_num, currentStock.getCurrent_quantity(), quantity, newQuantity);
			
		} catch (Exception e) {
			logger.error("재고 출고 처리 중 오류 발생: 상품번호={}, 수량={}, 오류={}", product_num, quantity, e.getMessage(), e);
			throw new RuntimeException("재고 출고 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
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