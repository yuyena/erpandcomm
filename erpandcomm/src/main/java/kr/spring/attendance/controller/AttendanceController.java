package kr.spring.attendance.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.spring.attendance.service.AttendanceService;
import kr.spring.attendance.vo.AttendanceVO;
import kr.spring.member.vo.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class AttendanceController {
	@Autowired
	private AttendanceService attendanceService;
	
	// 자바빈 초기화
	@ModelAttribute
	public AttendanceVO initCommand() {
		return new AttendanceVO();
	}
	// 근태 등록 폼
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/attendanceForm")
	public String form(@AuthenticationPrincipal PrincipalDetails principal,
			            Model model) {
		// 로그인 사용자 정보를 모델에 추가
		model.addAttribute("memberVO", principal.getMemberVO());
		
		return "views/personnel/attendanceForm";
	}
	
	// 근태 등록처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/attendanceForm")
	public String submit(@Valid AttendanceVO attendanceVO,
			             BindingResult result,
			             HttpServletRequest request,
			             @AuthenticationPrincipal PrincipalDetails principal,
						 Model model) throws IllegalStateException, IOException{
		log.debug("<<근태등록>> : {}", attendanceVO);
		
		// 근태 등록
		attendanceService.insertAttendance(attendanceVO);
		
		model.addAttribute("message","근태를 정상적으로 등록했습니다.");
		model.addAttribute("url", request.getContextPath()+"/personnel/attendance");
		
		return "views/common/resultAlert";
	}
	
	
}
