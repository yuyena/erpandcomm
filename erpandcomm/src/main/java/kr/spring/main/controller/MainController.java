package kr.spring.main.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.spring.member.vo.PrincipalDetails;
import kr.spring.member.vo.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import kr.spring.notice.service.NoticeService;
import java.util.List;
import kr.spring.notice.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
    @Autowired
    private NoticeService noticeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        MemberVO loginMember = principal != null ? principal.getMemberVO() : null;
        model.addAttribute("loginMember", loginMember);
        java.util.Map<String, Object> noticeMap = new java.util.HashMap<>();
        noticeMap.put("order", 1);
        noticeMap.put("start", 1);
        noticeMap.put("end", 3);
        java.util.List<NoticeVO> noticeList = noticeService.selectList(noticeMap);
        model.addAttribute("noticeList", noticeList);
        return "views/main/main";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/main/main")
    public String main(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        MemberVO loginMember = principal != null ? principal.getMemberVO() : null;
        model.addAttribute("loginMember", loginMember);
        java.util.Map<String, Object> noticeMap = new java.util.HashMap<>();
        noticeMap.put("order", 1);
        noticeMap.put("start", 1);
        noticeMap.put("end", 3);
        java.util.List<NoticeVO> noticeList = noticeService.selectList(noticeMap);
        model.addAttribute("noticeList", noticeList);
        return "views/main/main";
    }
	
	// 관리자 페이지
	@GetMapping("/main/admin")
	public String adminMain(Model model) {
		return "views/main/admin";
	}
	
	@GetMapping("/accessDenied")
	public String access() {
		return "views/common/accessDenied";
	}
	
	@GetMapping("/fileSizeLimit")
	public String fileSizeLimit() {
		return "views/common/fileSizeLimit";
	}

}









































