package kr.spring.payroll.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.payroll.service.PayrollService;
import kr.spring.payroll.vo.PayrollVO;
import kr.spring.util.ValidationUtil;
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
	
	// 급여 등록 처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/payrollForm")
	public String submitForm(@Valid PayrollVO payroll,
			                  BindingResult result,
			                  HttpServletRequest request,
			                  @AuthenticationPrincipal PrincipalDetails principal,
			                  Model model) throws IllegalStateException, IOException{
		log.debug("<<급여 등록>>:{}", payroll);
		// 유효성 검사 실패 시 다시
		if(result.hasErrors()) {
			model.addAttribute("payroll", principal.getMemberVO());
			
			ValidationUtil.printErrorFields(result);
			return "views/personnel/payrollForm";
		}
		// 급여 등록(insert)
		payrollService.insertPayroll(payroll);
		
		model.addAttribute("message", "급여를 정상적으로 등록했습니다.");
		model.addAttribute("url",request.getContextPath()+"/personnel/payrollLsit");
		
		return "views/common/resultAlert";
		
	}
}
