package kr.spring.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.spring.chat.service.ChatService;
import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.chat.vo.ChatMessageReadVO;
import kr.spring.chat.vo.ChatMessageVO;
import kr.spring.chat.vo.ChatRoomVO;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatUserController {
	
	@Autowired
	private ChatService chatService;
	@Autowired
	private MemberService memberService;
	
	// 자바빈 초기화
	@ModelAttribute
	public ChatRoomVO initChatRoom() {
		return new ChatRoomVO();
	}
	@ModelAttribute
	public ChatMemberVO initChatMember() {
		return new ChatMemberVO();
	}
	@ModelAttribute
	public ChatMessageVO initChatMessage() {
		return new ChatMessageVO();
	}
	@ModelAttribute
	public ChatMessageReadVO initChatMessageRead() {
		return new ChatMessageReadVO();
	}
	
	// 채팅방 생성 폼
//	@PreAuthorize("isAuthenticated()")
	@GetMapping("/createRoom")
	public String form() {
		return "views/chat/createRoom";
	}
	
	// 채팅방 생성
	
	
	// 채팅방 목록
//	@PreAuthorize("isAuthenticated()")
	@GetMapping("/roomList")
	public String getRoomList(Model model) {
		
	    // euser 테이블에서 사용자 목록 조회
	    Map<String, Object> map = new HashMap<String, Object>();
	    List<MemberVO> userList = memberService.selectMemberList(map);
		
	    // 임시로 사용자 번호를 직접 지정 (예: 1번 사용자)
	    long user_num = 1;
	    Map<String, Object> userMap = new HashMap<String, Object>();
	    userMap.put("member_num", user_num);

	    // 서비스에서 채팅방 목록 조회
	    List<ChatRoomVO> chatRoomList = chatService.selectListChatRoom(userMap);

	    // 모델에 담기
	    model.addAttribute("userList", userList);
	    model.addAttribute("chatRoomList", chatRoomList);
	    
	    log.debug("<<회원 목록>> : " + map);
		
		return "views/chat/chatRoomList";
	}

}









































