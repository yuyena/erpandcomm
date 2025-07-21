package kr.spring.notice.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.spring.member.vo.PrincipalDetails;
import kr.spring.notice.service.NoticeService;
import kr.spring.notice.vo.NoticeVO;
import kr.spring.util.PagingUtil;
import kr.spring.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/notice")
public class NoticeUserController {
	
	@Autowired
	private NoticeService noticeService;
	
	@ModelAttribute
	public NoticeVO initCommand() {
		return new NoticeVO();
	};
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/noticeList")
	public String getList(@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			     		  @RequestParam(value="order", defaultValue="1") int order,
			     		  @RequestParam(value="keyfield", required=false) String keyfield, 
			     		  @RequestParam(value="keyword", required=false) String keyword, 
			     		  Model model) {
		
		log.debug("<<공지사항>> order:{}",order);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keyfield", keyfield);
		map.put("keyword", keyword);
		
		log.debug("<<공지사항>> 검색 조건 - keyfield:{}, keyword:{}", keyfield, keyword);
		
		// 전체/검색 레코드 수
		Integer countInteger = noticeService.selectRowCount(keyfield, keyword);
		int count = countInteger != null ? countInteger.intValue() : 0;
		log.debug("<<공지사항>> 전체 레코드 수:{}", count);
		
		// 페이지 처리
		PagingUtil page = new PagingUtil(keyfield, keyword, pageNum, count, 20, 10, "noticeList", "&order="+order);
		List<NoticeVO> list = null;
		if(count > 0) {
			map.put("order", order);
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			
			log.debug("<<공지사항>> 페이지 범위 - start:{}, end:{}", page.getStartRow(), page.getEndRow());
			
			list = noticeService.selectList(map);
			log.debug("<<공지사항>> 조회된 리스트 크기:{}", list != null ? list.size() : "null");
		}
		
		model.addAttribute("count", count);
		model.addAttribute("list", list);
		model.addAttribute("page", page.getPage());
		model.addAttribute("order", order);
		model.addAttribute("keyfield", keyfield);
		model.addAttribute("keyword", keyword);
		
		return "views/notice/noticeList";
	}

	@PreAuthorize("hasAuthority('MANAGER')")
	@GetMapping("/write")
	public String writeForm(Model model) {
		log.debug("<<공지사항 작성 폼>>");
		return "views/notice/noticeForm";
	}
	
	@PreAuthorize("hasAuthority('MANAGER')")
	@PostMapping("/write")
	public String write(@ModelAttribute NoticeVO noticeVO,
						@AuthenticationPrincipal PrincipalDetails principal,
						RedirectAttributes redirectAttributes) {
		
		log.debug("<<공지사항 작성>> 제목:{}, 내용:{}", noticeVO.getNoti_title(), noticeVO.getNoti_content());
		
		// 작성자 정보 설정
		noticeVO.setUser_num(principal.getMemberVO().getUser_num());
		
		// 공지사항 저장
		noticeService.insertNotice(noticeVO);
		
		redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 등록되었습니다.");
		
		return "redirect:/notice/noticeList";
	}

    // 공지사항 수정 폼 진입
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update")
    public String noticeModifyForm(@RequestParam long noti_num,
                                  @AuthenticationPrincipal PrincipalDetails principal,
                                  Model model) {
        NoticeVO noticeVO = noticeService.selectNotice(noti_num);
        // 작성자만 접근 가능
        if (principal.getMemberVO().getUser_num() != noticeVO.getUser_num()) {
            // 로그인한 회원번호와 작성한 회원번호 불일치
            return "views/common/accessDenied";
        }
        model.addAttribute("noticeVO", noticeVO);
        return "views/notice/noticeModifyForm";
    }

    // 공지사항 수정 처리
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public String updateNotice(@Valid NoticeVO noticeVO, BindingResult result, HttpServletRequest request,
                              Model model, @AuthenticationPrincipal PrincipalDetails principal) throws IllegalStateException, IOException {
    	
    	log.debug("<<공지 수정>> : {}", noticeVO);
    	
        NoticeVO db_notice = noticeService.selectNotice(noticeVO.getNoti_num());
        // 작성자만 수정 가능
        if (principal.getMemberVO().getUser_num() != db_notice.getUser_num()) {
            return "views/common/accessDenied";
        }
        
        // 유효성 체크 결과 오류가 있으면 폼 호출
        if (result.hasErrors()) {
			// 유효성 체크 결과 오류 필드 출력
        	ValidationUtil.printErrorFields(result);
        	return "views/notice/noticeModifyForm";
		} // if
        
        noticeService.updateNotice(noticeVO);
        
        model.addAttribute("message", "공지 수정 완료!");
        model.addAttribute("url", request.getContextPath()+"/notice/noticeList");
        
        return "views/common/resultAlert";
    }
    
    // 공지 삭제
    @PreAuthorize("isAuthenticated")
    @GetMapping("/delete")
    public String submitDelete(long noti_num, HttpServletRequest request,
    						   @AuthenticationPrincipal PrincipalDetails principal) {
    	
    	log.debug("<<공지 삭제>> noti_num : {}", noti_num);
    	
    	NoticeVO db_notice = noticeService.selectNotice(noti_num);
    	
    	if (principal.getMemberVO().getUser_num() != db_notice.getUser_num()) {
			return "views/common/accessDenied";
		} // if
    	
    	noticeService.deleteNotice(noti_num);
    	
    	return "redirect:/notice/noticeList";
    }

}












































