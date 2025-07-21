package kr.spring.product.controller;

import java.io.IOException;
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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.product.service.ProductService;
import kr.spring.product.vo.ProductVO;
import kr.spring.util.ExcelUtil;
import kr.spring.util.FileUtil;
import kr.spring.util.PagingUtil;
import kr.spring.util.StringUtil;
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
	//상품 등록 폼
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/register")
	public String form(Model model) {
		List<Map<String, Object>> categoryList = productService.selectCategoryList();
		model.addAttribute("categoryList", categoryList);
		
		return "views/product/productRegister";
	}
	
	//글등록 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/register")
	public String submit(@Valid ProductVO productVO,
			             BindingResult result,
			             HttpServletRequest request,
			             @AuthenticationPrincipal
			             PrincipalDetails principal,
			             Model model) 
			            		 throws IllegalStateException, 
			            		               IOException {
		
		log.debug("<<상품 등록>> : {}",productVO);
		
		//유효성 체크 결과 오류가 있으면 폼 호출
		if(result.hasErrors()) {
			//유효성 체크 결과 오류 필드 출력
			ValidationUtil.printErrorFields(result);
			return "views/product/productRegister";
		}		
		
		//상품등록
		productService.insertProduct(productVO);
		
		model.addAttribute("message", "상품을 정상적으로 등록했습니다.");
		model.addAttribute("url", request.getContextPath()+"/product/productView");
		
		return "views/common/resultAlert";
	}
	
	//상품 상세
	@GetMapping("/detail")
	public String getDetail(@RequestParam("product_num") long product_num, Model model) {
		
		log.debug("<<상품 상세>> product_num : {}",product_num);
	
		ProductVO productVO = productService.selectProduct(product_num);
		model.addAttribute("productVO", productVO);
		
		return "views/product/productDetail";
	}
	//게시판 수정 폼
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update")
	public String formUpdate(@RequestParam("product_num") long product_num,
			                 Model model,
			       @AuthenticationPrincipal
			       PrincipalDetails principal) {
		
		ProductVO productVO = productService.selectProduct(product_num);
		//
//		if(principal.getMemberVO().getMem_num() != 
//				               boardVO.getMem_num()) {
//			//로그인한 회원번호와 작성자 회원번호 불일치
//			return "views/common/accessDenied";
//		}
		List<Map<String, Object>> categoryList = productService.selectCategoryList();
		
		model.addAttribute("productVO", productVO);		
		model.addAttribute("categoryList", categoryList);
		return "views/product/productUpdate";
	}
	
	//상품 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update")
	public String submitUpdate(@Valid ProductVO productVO,
			        BindingResult result,
			        HttpServletRequest request,
			        Model model,
			        @AuthenticationPrincipal
			        PrincipalDetails principal) 
			        		throws IllegalStateException, IOException {
		log.debug("<<상품 수정>> : {}", productVO);
		
		ProductVO db_board = productService.selectProduct(productVO.getProduct_num());
		//로그인한 회원번호와 작성자 회원번호 일치 여부 체크
//		if(principal.getMemberVO().getMem_num() != 
//				             db_board.getMem_num()) {
//			return "views/common/accessDenied";
//		}
		
		//유효성 체크 결과 오류가 있으면 폼 호출
//		if(result.hasErrors()) {
//			//유효성 체크 결과 오류 필드 출력
//			ValidationUtil.printErrorFields(result);
//			//title또는 content가 입력되지 않아 유효성 체크에
//			//걸리면 파일 정보를 잃어버리기 때문에 폼을 호출할 때
//			//파일을 다시 셋팅
//			boardVO.setFilename(db_board.getFilename());
//			return "views/board/boardModify";
//		}
		//상품 수정
		productService.updateProduct(productVO);
		
		model.addAttribute("message", "상품 정보 수정이 완료되었습니다.");
		model.addAttribute("url", request.getContextPath()+"/product/detail?product_num="
						             +productVO.getProduct_num());
		
		return "views/common/resultAlert";
	}
	@GetMapping("/excel")
	public void downloadExcel(
	    HttpServletResponse response,
	    @RequestParam(value = "category_num", required = false) String categoryNum,
	    @RequestParam(value = "keyfield", required = false) String keyfield,
	    @RequestParam(value = "keyword", required = false) String keyword,
	    @RequestParam(value = "min_price", required = false) Integer minPrice,
	    @RequestParam(value = "max_price", required = false) Integer maxPrice,
	    @RequestParam(value = "order", defaultValue = "1") int order
	) throws IOException {
	    
	    // 검색 조건으로 데이터 조회 (기존 productView와 동일한 로직)
	    Map<String, Object> map = new HashMap<>();
	    map.put("category_num", categoryNum);
	    map.put("keyfield", keyfield);
	    map.put("keyword", keyword);
	    map.put("min_price", minPrice);
	    map.put("max_price", maxPrice);
	    map.put("order", order);
	    map.put("start", 1);
	    map.put("end", 999999);
	    List<ProductVO> productList = productService.selectList(map);
	    
	    // 엑셀 파일 생성
	    ExcelUtil.createProductExcel(response, productList);
	}
}
