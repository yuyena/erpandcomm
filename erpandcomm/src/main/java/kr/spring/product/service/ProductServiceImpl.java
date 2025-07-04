package kr.spring.product.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.member.dao.MemberMapper;
import kr.spring.member.vo.MemberVO;
import kr.spring.product.vo.ProductVO;
import kr.spring.product.dao.ProductMapper;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {


	@Autowired
	private ProductMapper productMapper;
	
	
	@Override
	public List<ProductVO> selectList(Map<String, Object> map) {
		return productMapper.selectList(map);
	}

	@Override
	public Integer selectRowCount(Map<String, Object> map) {
		return productMapper.selectRowCount(map);
	}

	@Override
	public ProductVO selectProduct(Long product_num) {
		return productMapper.selectProduct(product_num);
	}

	@Override
	public List<Map<String, Object>> selectCategoryList() {
		return productMapper.selectCategoryList();
	}


	
	
	
	
	
	@Override
	public List<ProductVO> selectProductList() {
		return productMapper.selectProductList();
	}

	

}













































