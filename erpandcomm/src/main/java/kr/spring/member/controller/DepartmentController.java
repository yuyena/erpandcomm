package kr.spring.member.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.spring.member.service.DepartmentService;
import kr.spring.member.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/personnel")
@Slf4j
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    // 부서 등록 폼
    @GetMapping("/department/register")
    public String departmentRegisterForm(Model model) {
        DepartmentVO department = new DepartmentVO();
        model.addAttribute("department", department);
        return "views/personnel/department_register";
    }
    
    // 부서 수정 폼
    @GetMapping("/department/edit")
    public String departmentEditForm() {
        return "views/personnel/department_edit";
    }
    
    // 부서 등록 처리
    @PostMapping("/department/register")
    public String departmentRegister(DepartmentVO department, Model model) {
        try {
            departmentService.insertDepartment(department);
            model.addAttribute("accessTitle", "부서 등록");
            model.addAttribute("accessMsg", "부서가 등록되었습니다.");
            model.addAttribute("accessUrl", "/personnel/department");
            model.addAttribute("accessBtn", "이동");
            return "views/common/resultView";
        } catch (Exception e) {
            log.error("부서 등록 오류: {}", e.getMessage());
            model.addAttribute("accessTitle", "부서 등록");
            model.addAttribute("accessMsg", "부서 등록에 실패했습니다: " + e.getMessage());
            model.addAttribute("accessUrl", "/personnel/department/register");
            model.addAttribute("accessBtn", "다시 시도");
            return "views/common/resultView";
        }
    }
    
    // 부서 수정 폼
    @GetMapping("/department/modify/{departmentNum}")
    public String departmentModifyForm(@PathVariable("departmentNum") Long departmentNum, Model model) {
        DepartmentVO department = departmentService.selectDepartment(departmentNum);
        if (department != null) {
            model.addAttribute("department", department);
        }
        return "views/personnel/department_modify";
    }
    
    // 부서 수정 처리
    @PostMapping("/department/modify")
    public String departmentModify(DepartmentVO department, Model model) {
        try {
            departmentService.updateDepartment(department);
            model.addAttribute("accessTitle", "부서 수정");
            model.addAttribute("accessMsg", "부서가 수정되었습니다.");
            model.addAttribute("accessUrl", "/personnel/department");
            model.addAttribute("accessBtn", "이동");
            return "views/common/resultView";
        } catch (Exception e) {
            log.error("부서 수정 오류: {}", e.getMessage());
            model.addAttribute("accessTitle", "부서 수정");
            model.addAttribute("accessMsg", "부서 수정에 실패했습니다: " + e.getMessage());
            model.addAttribute("accessUrl", "/personnel/department/modify/" + department.getDepartment_num());
            model.addAttribute("accessBtn", "다시 시도");
            return "views/common/resultView";
        }
    }
    
    // 부서 삭제 처리
    @PostMapping("/department/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public Map<String, Object> departmentDelete(@RequestParam Long departmentNum) {
        try {
            departmentService.deleteDepartment(departmentNum);
            return Map.of("result", "success", "message", "부서가 삭제되었습니다.");
        } catch (Exception e) {
            log.error("부서 삭제 오류: {}", e.getMessage());
            return Map.of("result", "error", "message", "부서 삭제에 실패했습니다: " + e.getMessage());
        }
    }
    
    // 부서 목록 조회
    @GetMapping("/department/list")
    @ResponseBody
    public Map<String, Object> departmentList(@RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) String keyword) {
        try {
            Map<String, Object> map = new HashMap<>();
            if (page == null) page = 1;
            map.put("start", (page - 1) * 10 + 1);
            map.put("end", page * 10);
            map.put("keyword", keyword);
            
            List<DepartmentVO> list = departmentService.selectDepartmentList(map);
            Integer total = departmentService.selectDepartmentCount();
            
            return Map.of("list", list, "total", total);
        } catch (Exception e) {
            log.error("부서 목록 조회 오류: {}", e.getMessage());
            return Map.of("error", e.getMessage());
        }
    }
}
