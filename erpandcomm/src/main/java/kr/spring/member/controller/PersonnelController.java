package kr.spring.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import kr.spring.member.service.DepartmentService;
import kr.spring.member.service.GradeService;
import kr.spring.member.service.PositionService;
import kr.spring.member.vo.DepartmentVO;
import kr.spring.member.vo.GradeVO;
import kr.spring.member.vo.PositionVO;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/personnel")
public class PersonnelController {
    
    @GetMapping("/department")
    public String department() {
        return "views/personnel/department";
    }
    
    @GetMapping("/employee")
    public String employee() {
        return "views/personnel/employee";
    }

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PositionService positionService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/employee/register")
    public String employeeRegister(Model model) {
        // 부서 목록 조회
        List<DepartmentVO> departmentList = departmentService.selectDepartmentList(null);
        model.addAttribute("departmentList", departmentList);
        // 직급 목록 조회
        List<PositionVO> positionList = positionService.selectPositionList();
        model.addAttribute("positionList", positionList);
        // 등급 목록 조회
        List<GradeVO> gradeList = gradeService.selectGradeList();
        model.addAttribute("gradeList", gradeList);
        return "views/personnel/employee_register";
    }

    @PostMapping("/employee/register")
    public String registerEmployee(
            @RequestParam("userName") String userName,
            @RequestParam("passwd") String passwd,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            @RequestParam("hireDate") String hireDate,
            @RequestParam("resignationDate") String resignationDate,
            @RequestParam("salary") Integer salary,
            @RequestParam("departmentNum") String departmentNum,
            @RequestParam("positionNum") String positionNum,
            @RequestParam("gradeNum") String gradeNum,
            @RequestParam("extensionNum") String extensionNum,
            @RequestParam("residentRegNum") String residentRegNum,
            Model model
    ) throws Exception {
        // 1. 사번, 사원코드, 권한 생성 (간단히 예시)
        String employeeCode = "EMP" + System.currentTimeMillis();
        String authority = "ROLE_USER";

        // 2. 사진 파일명 저장 (실제 파일 저장 생략, 파일명만 저장)
        String photoName = (photo != null && !photo.isEmpty()) ? photo.getOriginalFilename() : null;

        // 3. EUSER 저장 (예시)
        // 실제로는 서비스/DAO에서 DB에 저장해야 함
        // EUserDTO euser = new EUserDTO();
        // euser.setEmployeeCode(employeeCode);
        // euser.setAuthority(authority);
        // euserService.insertEUser(euser);

        // 4. EUSER_DETAIL 저장 (예시)
        // EUserDetailDTO detail = new EUserDetailDTO();
        // detail.setUserName(userName); ... 등등
        // euserDetailService.insertEUserDetail(detail);

        // 5. 완료 메시지 및 목록 리다이렉트
        model.addAttribute("message", "직원 등록이 완료되었습니다.");
        model.addAttribute("url", "/personnel/employee");
        return "views/common/resultAlert";
    }
    
    @GetMapping("/attendance")
    public String attendance() {
        return "views/personnel/attendance";
    }
    
    @GetMapping("/vacation")
    public String vacation() {
        return "views/personnel/vacation";
    }
    
    @GetMapping("/salary")
    public String salary() {
        return "views/personnel/salary";
    }
    
    @GetMapping("/evaluation")
    public String evaluation() {
        return "views/personnel/evaluation";
    }
    
    @GetMapping("/statistics")
    public String statistics() {
        return "views/personnel/statistics";
    }
}
