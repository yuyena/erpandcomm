package kr.spring.attendance.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.spring.attendance.service.AttendanceService;
import kr.spring.attendance.vo.AttendanceVO;
import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping("/personnel")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;
	
	// 자바빈 초기화
	@ModelAttribute
	public AttendanceVO initCommand() {
		return new AttendanceVO();
	}
	// 근태 목록 조회
	@GetMapping("/attendanceList")
	public String getAttendanceListByEmpId(@AuthenticationPrincipal PrincipalDetails principal,
			                               Model model) {
		Long empId = principal.getMemberVO().getUser_num(); // 로그인 한 사용자의 empId
		Map<String, Object> map = new HashMap<>();
		map.put("empId", empId); // 이 empId로 해당 직원의 근태 목록만 조회
		
		MemberVO memberVO = attendanceService.selectList(map);
		model.addAttribute("memberVO", memberVO);
		return "views/personnel/attendanceList"; //리스트 보여줄 뷰
	}
	
	// 근태 등록 폼
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/Form")
	public String form(@AuthenticationPrincipal PrincipalDetails principal,
			            Model model) {
		MemberVO member = principal.getMemberVO();
		
		log.debug("MemberVO:{}", member);
		
		// 로그인 사용자 정보를 모델에 추가
		model.addAttribute("memberVO", member);
		model.addAttribute("today", new Date());
		
		return "views/personnel/attendanceForm";
	}
	
	// 근태 등록처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/Form")
	public String submit(@Valid AttendanceVO attendanceVO,
			             BindingResult result,
			             HttpServletRequest request,
			             @AuthenticationPrincipal PrincipalDetails principal,
						 Model model) throws IllegalStateException, IOException{
		
		log.debug("<<근태등록>> : {}", attendanceVO);
		
		// 유효성 검사 실패 시 다시
		if(result.hasErrors()) {
			
			ValidationUtil.printErrorFields(result);
			
			return form(principal,model);
		}
		
		Long userNum = principal.getUserNum();
		attendanceVO.setEmpId(userNum); // empId 셋팅
		//String employeeCode = principal.getMemberVO().getEmployee_code(); // 또는 principal.getUsername()
		
		String employeeCode = principal.getUsername();
		attendanceVO.setEmployeeCode(employeeCode);
		
		String userName = principal.getMemberVO().getUser_name(); // 이렇게 꺼내면 됨
		attendanceVO.setEmpName(userName);
		
		// 근태 등록
		attendanceService.insertAttendance(attendanceVO);
		
		model.addAttribute("message","근태를 정상적으로 등록했습니다.");
		model.addAttribute("url", request.getContextPath()+"/personnel/attendanceList");
		
		return "views/common/resultAlert";
	}
}
