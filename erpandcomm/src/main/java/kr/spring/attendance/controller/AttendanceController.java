package kr.spring.attendance.controller;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import groovy.util.logging.Slf4j;
import jakarta.validation.Valid;
import kr.spring.attendance.service.AttendanceService;
import kr.spring.attendance.vo.AttendanceVO;

@Slf4j
@Controller
@RequestMapping("/attendance")
public class AttendanceController {
	@Autowired
	private AttendanceService attendanceService;
	
	//자바빈(VO)초기화
	@ModelAttribute
	public AttendanceVO initCommand() {
		return new AttendanceVO();
	}
	
	// 근태 등록 폼
	@PostMapping("/attendance/insert")
	@PreAuthorize("isAuthenticated()") 
	public String insertForm(@Valid @ModelAttribute AttendanceVO attendanceVO,
			                  BindingResult result,
			                  Model model) {
		if(result.hasErrors()) {
			model.addAttribute("error","입력값이 잘못되었습니다.");
			return "views/personnel/attendance";
		}
		if(attendanceService.isDuplicate(attendanceVO)) {
			model.addAttribute("message", "이미 등록된 근태 정보입니다");
			model.addAttribute("url","/attendance");
		}
		
		attendanceService.insertAttendance(attendanceVO);
		return "redirect:/attendance";
	}
	
	
	
}
