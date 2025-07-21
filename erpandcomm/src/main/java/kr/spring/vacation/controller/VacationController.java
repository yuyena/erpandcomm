package kr.spring.vacation.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.apache.catalina.filters.ExpiresFilter.XHttpServletResponse;
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
import kr.spring.util.ValidationUtil;
import kr.spring.vacation.service.VacationService;
import kr.spring.vacation.vo.VacationVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/personnel")
public class VacationController {
	@Autowired
	private VacationService vacationService;
	
	// 자바빈 초기화
	@ModelAttribute
	private VacationVO initCommand() {
		return new VacationVO();
	}
	
	// 휴가 등록 폼
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vacationForm")
	public String form(@AuthenticationPrincipal PrincipalDetails principal,
			            Model model) {
		MemberVO member = principal.getMemberVO();
		
		log.debug("MemberVO: {}", member);
		
		//로그인 사용자 정보를 모델에 추가
		model.addAttribute("memberVO", member);
		
		return "views/personnel/vacationForm";
	}
	// 휴가 등록처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/vacationForm")
	public String submit(@Valid VacationVO vacationVO,
			              BindingResult result,
			              HttpServletRequest request,
			              @AuthenticationPrincipal PrincipalDetails principal,
			              Model model)throws IllegalStateException, IOException{
		log.debug("<<휴가등록>> : {}", vacationVO);
		//  유효성 검사 실패 시 다시
		if(result.hasErrors()) {
			model.addAttribute("vacationVO", principal.getMemberVO());
			
			ValidationUtil.printErrorFields(result);
			return "views/personnel/vacationForm";
		}
		
		// 휴가 등록
		vacationService.insertVacation(vacationVO);
		
		model.addAttribute("message", "휴가를 정상적으로 등록 했습니다.");
		model.addAttribute("url", request.getContextPath()+"/personnel/vacationList");
		
		return "views/common/resultAlert";
	}
	
		// 휴가 목록조회
		// selectVacationList
		@GetMapping("/vacationList")
		public String getVacationListByEmpId(@AuthenticationPrincipal PrincipalDetails principal,
				                            Model model) {
			// 로그인 한 사용자의  empId
			Long empId = principal.getMemberVO().getUser_num();
			
			// empId 하나만 넘기기
			List<VacationVO> vlist = vacationService.selectVacationList(empId); // service로 empId넘김
			model.addAttribute("vlist",vlist);
			
			// 페이지 처리
			return "views/personnel/vacationList"; // 리스트 보여주는 뷰
		}
			              
	
	
	
	
	
	
}
