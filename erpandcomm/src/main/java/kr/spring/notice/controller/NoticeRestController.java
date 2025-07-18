package kr.spring.notice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.spring.member.vo.PrincipalDetails;
import kr.spring.notice.service.NoticeService;
import kr.spring.notice.vo.NoticeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeRestController {
	
	@Autowired
	private NoticeService noticeService;
	
	@PreAuthorize("isAuthenticated")
	@GetMapping("/noticeDetail/{noti_num}")
	public ResponseEntity<Map<String, Object>> getDetail(@PathVariable("noti_num") long noti_num,
														 @AuthenticationPrincipal PrincipalDetails principal) {
		
		log.debug("<<공지사항 상세>> noti_num:{}, user:{}",noti_num,principal.getMemberVO().getUser_name());
		
		Map<String, Object> mapAjax = new HashMap<String, Object>();
		
		NoticeVO notice = noticeService.selectNotice(noti_num);
		log.debug("<<공지사항 상세>> 조회 결과: {}", notice);
		
		if (notice != null) {
			mapAjax.put("result", "success");
			mapAjax.put("noti_content", notice.getNoti_content());
			mapAjax.put("noti_date", notice.getNoti_date());
			mapAjax.put("user_num", notice.getUser_num());
			mapAjax.put("author_name", notice.getMemberVO() != null ? notice.getMemberVO().getUser_name() : "작성자 없음");
		} else {
			mapAjax.put("result", "notFound");
			mapAjax.put("message", "존재하지 않는 공지사항입니다.");
		}
		
		return new ResponseEntity<Map<String, Object>>(mapAjax, HttpStatus.OK);
	}

}

















































