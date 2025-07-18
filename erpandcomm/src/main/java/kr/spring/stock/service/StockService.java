package kr.spring.stock.service;

import kr.spring.product.vo.ProductVO;
import kr.spring.stock.vo.StockMovementVO;

import java.util.List;
import java.util.Map;

public interface StockService {
	public Integer selectRowCountStock(Map<String, Object> map);
	public List<ProductVO> selectStockList(Map<String, Object> map);
	public Map<String, Object> selectStockStatusCount();
	public ProductVO selectStockDetail(int product_num);
	public void updateStock(Map<String, Object> map);
	public void insertStock(Map<String, Object> map);
	public void updateMinMaxStock(Map<String, Object> map);
	
	public StockMovementVO selectStockMovementHistory(long product_num);

	   
} 