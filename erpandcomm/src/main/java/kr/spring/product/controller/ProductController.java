package kr.spring.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.spring.member.security.CustomAccessDeniedHandler;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.product.service.ProductService;
import kr.spring.product.vo.ProductVO;
import kr.spring.util.FileUtil;
import kr.spring.util.PagingUtil;
import kr.spring.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	// 자바빈(VO) 초기화
	@ModelAttribute
	public ProductVO initCommand() {
		return new ProductVO();
	}
	
	
	@GetMapping("/productView")
	public String getProductList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
	                           @RequestParam(value = "order", defaultValue = "1") int order,
	                           @RequestParam(value = "category_num", defaultValue = "") String category_num,
	                           @RequestParam(value = "keyfield", required = false) String keyfield,
	                           @RequestParam(value = "keyword", required = false) String keyword,
	                           @RequestParam(value = "min_price", required = false) String min_price,
	                           @RequestParam(value = "max_price", required = false) String max_price,
	                           Model model) {

	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("category_num", category_num);
	    map.put("keyfield", keyfield);
	    map.put("keyword", keyword);
	    map.put("min_price", min_price);
	    map.put("max_price", max_price);

	    // 전체, 검색 레코드수
	    int count = productService.selectRowCount(map);
	    log.debug("조회된 레코드 수: {}", count);

	    // 페이지 처리
	    PagingUtil page = new PagingUtil(keyfield, keyword, pageNum, count, 20, 10, "productView",
	            "&category_num=" + category_num + "&order=" + order);

	    List<ProductVO> list = null;
	    if(count > 0) {
	        map.put("order", order);
	        map.put("start", page.getStartRow());
	        map.put("end", page.getEndRow());

	        list = productService.selectList(map);
	    }

	    // 카테고리 목록 조회
	    List<Map<String, Object>> categoryList = productService.selectCategoryList();

	    model.addAttribute("count", count);
	    model.addAttribute("list", list);
	    model.addAttribute("page", page.getPage());
	    model.addAttribute("order", order);
	    model.addAttribute("keyfield", keyfield);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("category_num", category_num);
	    model.addAttribute("categoryList", categoryList);
	    model.addAttribute("min_price", min_price);
	    model.addAttribute("max_price", max_price);

	    log.debug("=== 제품 목록 조회 완료 ===");
	    return "views/product/productView";  
	}
}
