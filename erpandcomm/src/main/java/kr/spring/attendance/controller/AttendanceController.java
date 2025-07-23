package kr.spring.attendance.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


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
import kr.spring.attendance.service.AttendanceService;
import kr.spring.attendance.vo.AttendanceVO;
import kr.spring.attendance.vo.SearchVO;
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
	@GetMapping("/attendancesearch")
	public String getAttendanceByEmpId(@AuthenticationPrincipal PrincipalDetails principal,
			                           @RequestParam(value="startDate", required = false) String startDate,
			                           @RequestParam(value="endDate", required = false) String endDate,
			                           @RequestParam(value="empName", required = false) String empName,
			                           @RequestParam(value="status", required = false) String status,
			                           Model model) {
		// 로그인 한 사용자의 empId
		Long empId = principal.getMemberVO().getUser_num();
		
		// 조회조건 생성
		SearchVO searchVO = new SearchVO();
		searchVO.setEmpId(empId);
		searchVO.setEmpName(empName);
		searchVO.setStartDate(startDate);
		searchVO.setEndDate(endDate);
		searchVO.setStatus(status);
		
		log.debug("조회 empId: {}", empId);
		log.debug("serchVO:{}", searchVO);
		
		List<AttendanceVO> list = attendanceService.AttendanceSelectList(searchVO);
		
		// 모델에 담기
		model.addAttribute("list", list);
		model.addAttribute("searchVO", searchVO); // 검색조건도 같이 넘김(화면에서 유지)
		
		return "views/personnel/attendanceList";
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
			model.addAttribute("memberVO", principal.getMemberVO());
			
			ValidationUtil.printErrorFields(result);
			return "views/personnel/attendanceForm";
		}
		
		Long userNum = principal.getMemberVO().getUser_num();
		attendanceVO.setEmpId(userNum); // empId 셋팅
		
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
	
	// 근태 목록 조회
		@GetMapping("/attendanceList")
		public String getAttendanceListByEmpId(@AuthenticationPrincipal PrincipalDetails principal,
				                               Model model) {
			Long empId = principal.getMemberVO().getUser_num(); // 로그인 한 사용자의 empId
			
			// empId 하나만 넘기기
			List<AttendanceVO> list = attendanceService.selectAttedanceList(empId);
			model.addAttribute("list", list);
			
			// 페이지 처리
			return "views/personnel/attendanceList"; //리스트 보여줄 뷰
		}
		
		// 목록 보기 (오늘 근태 등록 전에도 볼 수 있는 목록)
		@GetMapping("/List")
		public String showList(@AuthenticationPrincipal PrincipalDetails principal,
				Model model) {
			Long empId = principal.getMemberVO().getUser_num();
			
			List<AttendanceVO> list = attendanceService.selectAttedanceList(empId);
			model.addAttribute("list",list);
			return "views/personnel/attendanceList";
		}
		
		// 근태 목록 수정 폼
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/updateForm")
		public String formUpdate(@RequestParam("attendanceId") long attendanceId,
								Model model,
								@AuthenticationPrincipal PrincipalDetails principal) {
		AttendanceVO attendance = attendanceService.selectAttendance(attendanceId);
		
		if(principal.getMemberVO().getUser_num() != attendance.getEmpId()) {
			return "views/common/accessDenide";
		}
		
		model.addAttribute("attendance", attendance);
		model.addAttribute("memberVO", principal.getMemberVO());
		
		return "views/personnel/attendanceUpdate";
		}
		
		// 근태 수정 처리
		@PreAuthorize("isAuthenticated()")
		@PostMapping("/update")
		public String submitUpdate(@Valid AttendanceVO attendance,
				BindingResult result,
				HttpServletRequest request,
				Model model,
				@AuthenticationPrincipal 
				PrincipalDetails principal) throws IllegalStateException, IOException{
			
			log.debug("<<근태 수정>> : {}", attendance);
			
			
			// emp_id수정?
			AttendanceVO attendanceVO = attendanceService.selectAttendance(attendance.getAttendanceId());
			
			// 로그인한 회원번호와 작성자 회원번호 일치 여부 체크
			if(principal.getMemberVO().getUser_num() != attendanceVO.getEmpId()) {
				return "views/common/accessDenied";
			}
			
			//유효성 검사
			//유효성 체크 결과 오류가 있으면 폼 호출
			if(result.hasErrors()) {
				// 유효성 체크 결과 오류 필드 출력
				ValidationUtil.printErrorFields(result);
				return form(principal,model);
			}
			
			Long userNum = principal.getMemberVO().getUser_num();
			attendanceVO.setEmpId(userNum); // empId 셋팅
			
			String employeeCode = principal.getUsername();
			attendanceVO.setEmployeeCode(employeeCode);
			
			String userName = principal.getMemberVO().getUser_name(); // 이렇게 꺼내면 됨
			attendanceVO.setEmpName(userName);
			
			// 수정 처리
			attendanceService.updateAttendance(attendanceVO);
			
			model.addAttribute("message", "근태 정보가 수정되었습니다.");
			model.addAttribute("url", request.getContextPath()+"/personnel/attendanceList");
			
			return "views/common/resultAlert";
		}
		
		//근태 목록 삭제
		@PreAuthorize("isAuthenticated()")
		@GetMapping("/delete")
		public String submitDelete(@RequestParam("attendanceId") long attendanceId,
				HttpServletRequest request,
				@AuthenticationPrincipal
				PrincipalDetails principal) {
			log.debug("<<근태 목록 삭제>> attendanceId:{}",attendanceId);
			
			// attendence VO가져오기 
			AttendanceVO attendance = attendanceService.selectAttendance(attendanceId);
			
			// 로그인한 회원번호와 근태 등록 회원번호 일치 여부 체크
			if(principal.getMemberVO().getUser_num() != attendance.getEmpId()) {
				return "views/common/accessDenied";
			}
			
			//글 삭제
			attendanceService.deleteAttendance(attendanceId);
			
			return "redirect:/personnel/attendanceList";
		}
		
		// 출근버튼 눌렀을 때 서버 저장
		@PostMapping("/clockIn")
		public String clockIn(@RequestParam("checkIntime") String checkIntime,
				              @RequestParam("checkOuttime") String checkOuttime,
				              @RequestParam("status") String status,
				              @AuthenticationPrincipal PrincipalDetails principal ) {
		Long userId = principal.getMemberVO().getUser_num();
		String userName = principal.getMemberVO().getUser_name();
		
		AttendanceVO attendance = new AttendanceVO();
		attendance.setEmpId(userId);
		attendance.setWorkDate(LocalDate.now().toString());
		attendance.setEmpName(userName);
		attendance.setCheckIntime(checkIntime);
		attendance.setCheckOuttime(checkOuttime); // 출근 시 null 또는 빈값일 수 있음
		attendance.setStatus(status); // 상태정보
		attendance.setWorkType("정상 근무");
		
		//서비스 호출로 DB에 저장
		attendanceService.insertAttendance(attendance);
		
		return "redirect:/personnel/attendanceList"; // 처리 이동 후 완료
		
		}
		
	
}
