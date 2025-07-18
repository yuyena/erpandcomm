package kr.spring.stock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.stock.vo.StockMovementVO;
import kr.spring.product.vo.ProductVO;
import kr.spring.stock.dao.StockMapper;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StockServiceImpl implements StockService {
	@Autowired
	private StockMapper stockMapper;
	
	@Override
	public Integer selectRowCountStock(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ProductVO> selectStockList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> selectStockStatusCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductVO selectStockDetail(int product_num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStock(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertStock(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMinMaxStock(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StockMovementVO selectStockMovementHistory(long product_num) {
		return stockMapper.selectStockMovementHistory(product_num);
	}
    
} 