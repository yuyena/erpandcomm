package kr.spring.stock.service;

import org.springframework.stereotype.Service;
import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {
    @Override
    public List<CurrentStockVO> getCurrentStockList() { return null; }
    @Override
    public StockMovementVO getStockMovement(Long movement_num) { return null; }
    @Override
    public void saveStockMovement(StockMovementVO stockMovementVO) {}
    @Override
    public void deleteStockMovement(Long movement_num) {}
} 