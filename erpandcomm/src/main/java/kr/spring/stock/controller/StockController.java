package kr.spring.stock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import kr.spring.product.vo.ProductVO;
import kr.spring.stock.dao.StockMapper;
import kr.spring.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/product")
public class StockController {
    
    
    @Autowired
    private StockMapper stockMapper;
    //자바빈(VO) 초기화
  	@ModelAttribute
  	public ProductVO initCommand() {
  		return new ProductVO();
  	}
    // 재고 목록 페이지
    @GetMapping("/stocklist")
    public String stockList(@RequestParam(value="pageNum", defaultValue="1") int currentPage,
                           @RequestParam(value="keyword", defaultValue="") String keyword,
                           @RequestParam(value="stock_status", defaultValue="") String stock_status,
                           Model model) {
        
        log.debug("<<재고 목록>> : " + keyword + ", 재고상태: " + stock_status);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword", keyword);
        map.put("stock_status", stock_status);
        
        // 전체 레코드 수
        int count = stockMapper.selectRowCountStock(map);
        
        // 페이지 처리
        PagingUtil page = new PagingUtil(currentPage, count, 10, 10, "list");
        
        List<ProductVO> list = null;
        if(count > 0) {
            map.put("start", page.getStartRow());
            map.put("end", page.getEndRow());
            
            list = stockMapper.selectStockList(map);
        }
        
        model.addAttribute("count", count);
        model.addAttribute("list", list);
        model.addAttribute("page", page.getPage());
        model.addAttribute("keyword", keyword);
        model.addAttribute("stock_status", stock_status);
        
        return "stock/stockList";
    }
    
    // 대시보드 페이지
    @GetMapping("/stock/dashboard")
    public String stockDashboard(Model model) {
        
        // 재고 상태별 개수 조회
        Map<String, Object> statusCount = stockMapper.selectStockStatusCount();
        model.addAttribute("statusCount", statusCount);
        
        return "stock/stockDashboard";
    }
    
    // 재고 상세 페이지
    @GetMapping("/stock/detail")
    public String stockDetail(@RequestParam("product_num") int product_num, Model model) {
        
        ProductVO product = stockMapper.selectStockDetail(product_num);
        model.addAttribute("product", product);
        
        return "stock/stockDetail";
    }
    
    // 재고 수정 페이지
    @GetMapping("/stock/update")
    public String stockUpdateForm(@RequestParam("product_num") int product_num, Model model) {
        
        ProductVO product = stockMapper.selectStockDetail(product_num);
        model.addAttribute("product", product);
        
        return "stock/stockUpdateForm";
    }
    
    // 재고 수정 처리
    @PostMapping("/stock/update")
    public String stockUpdate(@RequestParam("product_num") int product_num,
                             @RequestParam("current_quantity") int current_quantity,
                             @RequestParam("min_stock") int min_stock,
                             @RequestParam("max_stock") int max_stock,
                             HttpServletRequest request) {
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("product_num", product_num);
        map.put("current_quantity", current_quantity);
        
        // 재고 수량 업데이트
        stockMapper.updateStock(map);
        
        // 최소/최대 재고 업데이트
        Map<String, Object> stockMap = new HashMap<String, Object>();
        stockMap.put("product_num", product_num);
        stockMap.put("min_stock", min_stock);
        stockMap.put("max_stock", max_stock);
        stockMapper.updateMinMaxStock(stockMap);
        
        return "redirect:/stock/list";
    }
    
    // Ajax - 재고 상태별 개수 조회
    @GetMapping("/stock/statusCount")
    @ResponseBody
    public Map<String, Object> getStockStatusCount() {
        
        return stockMapper.selectStockStatusCount();
    }
    
    // Ajax - 재고 수량 업데이트
    @PostMapping("/stock/updateQuantity")
    @ResponseBody
    public Map<String, Object> updateStockQuantity(@RequestParam("product_num") int product_num,
                                                   @RequestParam("current_quantity") int current_quantity) {
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("product_num", product_num);
            map.put("current_quantity", current_quantity);
            
            stockMapper.updateStock(map);
            
            result.put("result", "success");
            result.put("message", "재고가 성공적으로 업데이트되었습니다.");
            
        } catch(Exception e) {
            log.error("재고 업데이트 오류", e);
            result.put("result", "failure");
            result.put("message", "재고 업데이트 중 오류가 발생했습니다.");
        }
        
        return result;
    }
    
    // Ajax - 재고 정보 추가
    @PostMapping("/stock/addStock")
    @ResponseBody
    public Map<String, Object> addStock(@RequestParam("product_num") int product_num,
                                       @RequestParam("current_quantity") int current_quantity) {
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("product_num", product_num);
            map.put("current_quantity", current_quantity);
            
            stockMapper.insertStock(map);
            
            result.put("result", "success");
            result.put("message", "재고 정보가 성공적으로 추가되었습니다.");
            
        } catch(Exception e) {
            log.error("재고 추가 오류", e);
            result.put("result", "failure");
            result.put("message", "재고 추가 중 오류가 발생했습니다.");
        }
        
        return result;
    }
    
    // 재고 부족 알림 페이지
    @GetMapping("/stock/lowStock")
    public String lowStockList(Model model) {
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stock_status", "LOW");
        
        List<ProductVO> lowStockList = stockMapper.selectStockList(map);
        model.addAttribute("lowStockList", lowStockList);
        
        return "stock/lowStockList";
    }
}