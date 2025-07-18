package kr.spring.notice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.spring.notice.service.NoticeService;
import kr.spring.notice.vo.NoticeVO;
import kr.spring.util.PagingUtil;
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
		
		// 전체/검색 레코드 수
		int count = noticeService.selectRowCount(map);
		
		// 페이지 처리
		PagingUtil page = new PagingUtil(keyfield, keyword, pageNum, count, 20, 10, "noticeList", "&order="+order);
		List<NoticeVO> list = null;
		if(count > 0) {
			map.put("order", order);
			map.put("start", page.getStartRow());
			map.put("end", page.getEndRow());
			
			list = noticeService.selectList(map);
		}
		
		model.addAttribute("count", count);
		model.addAttribute("list", list);
		model.addAttribute("page", page.getPage());
		model.addAttribute("order", order);
		model.addAttribute("keyfield", keyfield);
		model.addAttribute("keyword", keyword);
		
		return "views/notice/noticeList";
	}

}












































