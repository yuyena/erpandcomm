package kr.spring.stock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import kr.spring.stock.vo.StockMovementVO;
import kr.spring.stock.vo.CurrentStockVO;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {
    // Service 주입 예정

    @GetMapping("/current")
    public String currentStock(Model model) {
        // List<CurrentStockVO> list = stockService.getCurrentStockList();
        // model.addAttribute("list", list);
        return "views/stock/current";
    }

    @GetMapping("/movementForm")
    public String movementForm(@RequestParam(required = false) Long movement_num, Model model) {
        // StockMovementVO vo = (movement_num != null) ? stockService.getStockMovement(movement_num) : new StockMovementVO();
        // model.addAttribute("stockMovementVO", vo);
        return "views/stock/movementForm";
    }

    @PostMapping("/saveMovement")
    public String saveMovement(StockMovementVO stockMovementVO) {
        // stockService.saveStockMovement(stockMovementVO);
        return "redirect:/stock/current";
    }

    @GetMapping("/deleteMovement")
    public String deleteMovement(@RequestParam Long movement_num) {
        // stockService.deleteStockMovement(movement_num);
        return "redirect:/stock/current";
    }
} 