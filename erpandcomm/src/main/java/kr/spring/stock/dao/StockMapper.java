package kr.spring.stock.dao;

import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;
import java.util.List;

public interface StockMapper {
    List<CurrentStockVO> selectCurrentStockList();
    StockMovementVO selectStockMovement(Long movement_num);
    void insertStockMovement(StockMovementVO stockMovementVO);
    void updateStockMovement(StockMovementVO stockMovementVO);
    void deleteStockMovement(Long movement_num);
} 