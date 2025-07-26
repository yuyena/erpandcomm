package kr.spring.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.payroll.service.PayrollService;
import kr.spring.payroll.vo.PayrollVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/personnel")
public class PayrollController {
	@Autowired
	private PayrollService payrollService;
	
	// 자바빈 초기화
	@ModelAttribute
	public PayrollVO initCommand() {
		return new PayrollVO();
	}
	
	// 급여 등록 폼 - 여기는 무조건 사용자 정보 가져오는 곳
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/salaryForm")
	public String form(@AuthenticationPrincipal PrincipalDetails principal,
			           Model model) {
		MemberVO member = principal.getMemberVO();
		
		log.debug("MemberVO:{}", member);
		
		// 로그인 사용자 정보를 모델에 추가
		model.addAttribute("member", member);

		return "views/personnel/salaryForm";
	}
}
