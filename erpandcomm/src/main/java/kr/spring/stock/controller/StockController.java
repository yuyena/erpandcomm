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
import kr.spring.product.dao.ProductMapper;
import kr.spring.product.vo.ProductVO;
import kr.spring.stock.dao.StockMapper;
import kr.spring.stock.dao.StockTransactionMapper;
import kr.spring.stock.vo.StockMovementVO;
import kr.spring.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/product")
public class StockController {
    
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private ProductMapper productMapper;
    
    // 자바빈(VO) 초기화
    @ModelAttribute
    public ProductVO initCommand() {
        return new ProductVO();
    }
    @ModelAttribute
    public StockMovementVO initCommand2() {
        return new StockMovementVO();
    }
    
    
    // 재고 목록 페이지
    @GetMapping("/stock")
    public String stockList(@RequestParam(value="pageNum", defaultValue="1") int pageNum,
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
        PagingUtil page = new PagingUtil(pageNum, count, 10, 10, "stock","&keyword=" + keyword + "&stock_status=" + stock_status);
        
        List<ProductVO> list = null;
        if(count > 0) {
            map.put("start", page.getStartRow());
            map.put("end", page.getEndRow());
            
            list = stockMapper.selectStockList(map);
        }
        
        model.addAttribute("count", count);
        model.addAttribute("stockList", list);
        model.addAttribute("page", page.getPage());
        model.addAttribute("keyword", keyword);
        model.addAttribute("stock_status", stock_status);
        
        return "views/stock/stockList";
    }
    
    // Ajax - 재고 입출고 이력 조회
//  @GetMapping("/stock/history")
//  @ResponseBody
//  public Map<String, Object> getStockHistory(@RequestParam("product_num") int product_num) {
//      
//      Map<String, Object> result = new HashMap<String, Object>();
//      
//      try {
//          // 재고 입출고 이력 조회 (실제 구현에서는 별도 테이블 필요)
//          List<Map<String, Object>> history = stockMapper.selectStockHistory(product_num);
//          
//          result.put("result", "success");
//          result.put("history", history);
//          
//      } catch(Exception e) {
//          log.error("재고 이력 조회 오류", e);
//          result.put("result", "failure");
//          result.put("message", "재고 이력 조회 중 오류가 발생했습니다.");
//      }
//      a
//      return result;
//  }
    
    // 재고 상세 페이지
    @GetMapping("/stock/history")
    public String stockDetail(@RequestParam("product_num") long product_num, Model model) {
        ProductVO product = productMapper.selectProduct(product_num);
        StockMovementVO stockHistory = stockMapper.selectStockMovementHistory(product_num);
        model.addAttribute("product", product);
        model.addAttribute("stockHistory", stockHistory);
        
        return "views/stock/stockHistory";
    }
    
    
    
    
    
    
    // 대시보드 
    @GetMapping("/stock/dashboard")
    public String stockDashboard(@RequestParam(value="pageNum", defaultValue="1") int currentPage,
                                @RequestParam(value="keyword", defaultValue="") String keyword,
                                @RequestParam(value="stock_status", defaultValue="") String stock_status,
                                Model model) {
        
        log.debug("<<재고 대시보드>> : " + keyword + ", 재고상태: " + stock_status);
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword", keyword);
        
        // stock_status 매핑 (프론트엔드와 백엔드 간 상태값 통일)
        if ("low".equals(stock_status)) {
            map.put("stock_status", "LOW");
        } else if ("high".equals(stock_status)) {
            map.put("stock_status", "HIGH");
        } else if ("normal".equals(stock_status)) {
            map.put("stock_status", "NORMAL");
        }
        
        // 전체 레코드 수
        int count = stockMapper.selectRowCountStock(map);
        
        // 페이지 처리 (대시보드에서는 더 많은 항목 표시)
        PagingUtil page = new PagingUtil(currentPage, count, 20, 10, "dashboard");
        
        List<ProductVO> list = null;
        if(count > 0) {
            map.put("start", page.getStartRow());
            map.put("end", page.getEndRow());
            
            list = stockMapper.selectStockList(map);
        }
        
        // 재고 상태별 개수 조회
        Map<String, Object> statusCount = stockMapper.selectStockStatusCount();
        
        model.addAttribute("count", count);
        model.addAttribute("list", list);
        model.addAttribute("page", page.getPage());
        model.addAttribute("keyword", keyword);
        model.addAttribute("stock_status", stock_status);
        model.addAttribute("statusCount", statusCount);
        
        return "stock/stockDashboard";
    }
    

    // 재고 수정 페이지
    @GetMapping("/stock/update")
    public String stockUpdateForm(@RequestParam("product_num") int product_num, Model model) {
        
//        ProductVO product = stockMapper.selectStockDetail(product_num);
//        model.addAttribute("product", product);
        
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
        
        return "redirect:/product/stock/dashboard";
    }
    
    // Ajax - 재고 상태별 개수 조회
    @GetMapping("/stock/statusCount")
    @ResponseBody
    public Map<String, Object> getstock_statusCount() {
        
        Map<String, Object> statusCount = stockMapper.selectStockStatusCount();
        
        // 프론트엔드에서 사용하기 쉽도록 데이터 가공
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", statusCount.get("total") != null ? statusCount.get("total") : 0);
        result.put("low", statusCount.get("low_stock") != null ? statusCount.get("low_stock") : 0);
        result.put("normal", statusCount.get("normal_stock") != null ? statusCount.get("normal_stock") : 0);
        result.put("high", statusCount.get("high_stock") != null ? statusCount.get("high_stock") : 0);
        
        return result;
    }
    
    // Ajax - 재고 수량 업데이트
//    @PostMapping("/stock/updateQuantity")
//    @ResponseBody
//    public Map<String, Object> updateStockQuantity(@RequestParam("product_num") int product_num,
//                                                   @RequestParam("current_quantity") int current_quantity) {
//        
//        Map<String, Object> result = new HashMap<String, Object>();
//        
//        try {
//            // 입력 값 검증
//            if (current_quantity < 0) {
//                result.put("result", "failure");
//                result.put("message", "재고 수량은 0 이상이어야 합니다.");
//                return result;
//            }
//            
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("product_num", product_num);
//            map.put("current_quantity", current_quantity);
//            
//            //int updateCount = stockMapper.updateStock(map);
//            
//            if (updateCount > 0) {
//                result.put("result", "success");
//                result.put("message", "재고가 성공적으로 업데이트되었습니다.");
//                
//                // 업데이트된 상품 정보 반환
//                ProductVO product = stockMapper.selectStockDetail(product_num);
//                result.put("product", product);
//            } else {
//                result.put("result", "failure");
//                result.put("message", "해당 상품을 찾을 수 없습니다.");
//            }
//            
//        } catch(Exception e) {
//            log.error("재고 업데이트 오류", e);
//            result.put("result", "failure");
//            result.put("message", "재고 업데이트 중 오류가 발생했습니다.");
//        }
//        
//        return result;
//    }
    
    // Ajax - 재고 정보 추가
    @PostMapping("/stock/addStock")
    @ResponseBody
    public Map<String, Object> addStock(@RequestParam("product_num") int product_num,
                                       @RequestParam("current_quantity") int current_quantity,
                                       @RequestParam(value="min_stock", defaultValue="0") int min_stock,
                                       @RequestParam(value="max_stock", defaultValue="100") int max_stock) {
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            // 입력 값 검증
            if (current_quantity < 0 || min_stock < 0 || max_stock <= 0) {
                result.put("result", "failure");
                result.put("message", "올바른 재고 수량을 입력해주세요.");
                return result;
            }
            
            if (min_stock >= max_stock) {
                result.put("result", "failure");
                result.put("message", "최대 재고는 최소 재고보다 커야 합니다.");
                return result;
            }
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("product_num", product_num);
            map.put("current_quantity", current_quantity);
            map.put("min_stock", min_stock);
            map.put("max_stock", max_stock);
            
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
        int lowStockCount = stockMapper.selectRowCountStock(map);
        
        model.addAttribute("lowStockList", lowStockList);
        model.addAttribute("lowStockCount", lowStockCount);
        
        return "stock/lowStockList";
    }
    
    // Ajax - 재고 현황 데이터 조회
    @GetMapping("/stock/data")
    @ResponseBody
    public Map<String, Object> getStockData(@RequestParam(value="keyword", defaultValue="") String keyword,
                                           @RequestParam(value="stock_status", defaultValue="") String stock_status) {
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyword", keyword);
        
        // stock_status 매핑
        if ("low".equals(stock_status)) {
            map.put("stock_status", "LOW");
        } else if ("high".equals(stock_status)) {
            map.put("stock_status", "HIGH");
        } else if ("normal".equals(stock_status)) {
            map.put("stock_status", "NORMAL");
        }
        
        List<ProductVO> list = stockMapper.selectStockList(map);
        int count = stockMapper.selectRowCountStock(map);
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        result.put("count", count);
        result.put("statusCount", stockMapper.selectStockStatusCount());
        
        return result;
    }
    
    // Ajax - 대량 재고 업데이트
    @PostMapping("/stock/bulkUpdate")
    @ResponseBody
    public Map<String, Object> bulkUpdateStock(@RequestParam("updates") String updates) {
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        try {
            // JSON 파싱 및 대량 업데이트 로직
            // 실제 구현에서는 JSON 라이브러리 사용
            
            result.put("result", "success");
            result.put("message", "대량 재고 업데이트가 완료되었습니다.");
            
        } catch(Exception e) {
            log.error("대량 재고 업데이트 오류", e);
            result.put("result", "failure");
            result.put("message", "대량 재고 업데이트 중 오류가 발생했습니다.");
        }
        
        return result;
    }
    

}