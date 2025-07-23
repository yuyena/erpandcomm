package kr.spring.vacation.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.spring.attendance.vo.AttendanceVO;
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

	// 휴가 목록조회
	// selectVacationList
	@GetMapping("/vacationList")
	public String getVacationListByEmpId(@AuthenticationPrincipal PrincipalDetails principal, 							
										@RequestParam(value="startDate", required = false) String startDate,
										@RequestParam(value="endDate", required= false)String endDate,
										@RequestParam(value="vacationType", required = false) String vacationType,
										Model model) {
		// 로그인 한 사용자의 empId
		Long empId = principal.getMemberVO().getUser_num();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("empId", empId);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("vacationType", vacationType);
		
		// empId 하나만 넘기기
		List<VacationVO> vlist = vacationService.selectList(map); 
		log.debug("vlist:{}", vlist);
		
		model.addAttribute("vlist", vlist);

		// 페이지 처리
		return "views/personnel/vacationList"; // 리스트 보여주는 뷰
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
	public String submit(@Valid VacationVO vacation,
			              BindingResult result,
			              HttpServletRequest request,
			              @AuthenticationPrincipal PrincipalDetails principal,
			              Model model)throws IllegalStateException, IOException{
		log.debug("<<휴가등록>> : {}", vacation);
		//  유효성 검사 실패 시 다시
		if(result.hasErrors()) {
			model.addAttribute("vacation", principal.getMemberVO());
			
			ValidationUtil.printErrorFields(result);
			return "views/personnel/vacationForm";
		}
		
		// 휴가 등록
		vacationService.insertVacation(vacation);
		
		model.addAttribute("message", "휴가를 정상적으로 등록 했습니다.");
		model.addAttribute("url", request.getContextPath()+"/personnel/vacationList");
		
		return "views/common/resultAlert";
	}
	
	// 휴가 목록 삭제
	@PreAuthorize("isAuthenticated")
	@GetMapping("/vacationdelete")
	
	public String submitDelete(@RequestParam("requestId") long requestId,
			HttpServletRequest request,
			@AuthenticationPrincipal
			PrincipalDetails principal) {
		log.debug("<<휴가 목록 삭제>> requestId:{}",requestId);
		
		// vacation VO가져오기 
		VacationVO vacation = vacationService.selectVacationDetail(requestId);
		
		// 로그인한 회원번호와 근태 등록 회원번호 일치 여부 체크
		if(principal.getMemberVO().getUser_num() != vacation.getEmpId()) {
			return "views/common/accessDenied";
		}
		
		//글 삭제
		vacationService.deleteVacation(requestId);
		
		return "redirect:/personnel/vacationList";
	}
		
	// 휴가 일수 부여
	

	
}
