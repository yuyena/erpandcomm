package kr.spring.stock.service;

import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;
import java.util.List;

public interface StockService {
    List<CurrentStockVO> getCurrentStockList();
    StockMovementVO getStockMovement(Long movement_num);
    void saveStockMovement(StockMovementVO stockMovementVO);
    void deleteStockMovement(Long movement_num);
} 