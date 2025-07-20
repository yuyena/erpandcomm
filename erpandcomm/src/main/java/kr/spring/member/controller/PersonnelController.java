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
import kr.spring.member.service.EUserService;
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

    @Autowired
    private EUserService eUserService;

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
        // 사번(유저번호) 생성 예시 (실제 환경에 맞게 시퀀스 등으로 대체)
        long userNum = System.currentTimeMillis();
        // EMPLOYEE_CODE는 최대 12자, 앞 3자(EMP) + 9자리 숫자
        String employeeCode = "EMP" + ("" + userNum).substring(("" + userNum).length() - 9);
        String authority = "ROLE_USER";
        String photoName = (photo != null && !photo.isEmpty()) ? photo.getOriginalFilename() : null;

        // EUSER DTO 생성
        kr.spring.member.dto.EUserDTO euser = new kr.spring.member.dto.EUserDTO();
        euser.setUserNum(userNum);
        euser.setEmployeeCode(employeeCode);
        euser.setAuthority(authority);

        // EUSER_DETAIL DTO 생성
        kr.spring.member.dto.EUserDetailDTO detail = new kr.spring.member.dto.EUserDetailDTO();
        detail.setUserNum(userNum);
        detail.setUserName(userName);
        detail.setPasswd(passwd);
        detail.setPhone(phone);
        detail.setEmail(email);
        detail.setPhoto(photoName);
        detail.setHireDate(hireDate);
        detail.setResignationDate(resignationDate);
        detail.setSalary(salary);
        detail.setDepartmentNum(departmentNum);
        detail.setPositionNum(positionNum);
        detail.setGradeNum(gradeNum);
        detail.setExtensionNum(extensionNum);
        detail.setResidentRegNum(residentRegNum);

        // 실제 DB 저장
        eUserService.registerEmployee(euser, detail);

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
