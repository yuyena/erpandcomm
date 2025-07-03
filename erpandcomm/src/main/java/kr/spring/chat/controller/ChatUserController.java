package kr.spring.chat.controller;

import java.util.List;

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
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatUserController {
	
	@Autowired
	private ChatService chatService;
	
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
	
//	@PreAuthorize("isAuthenticated()")
	@GetMapping("/roomList")
	public String getRoomList(Model model) {
		
	    // 임시로 사용자 번호를 직접 지정 (예: 1번 사용자)
	    long user_num = 1;

	    // 서비스에서 채팅방 목록 조회
	    List<ChatRoomVO> chatRoomList = chatService.selectListChatRoom(user_num);

	    // 모델에 담기
	    model.addAttribute("chatRoomList", chatRoomList);
		
		return "views/chat/chatRoomList";
	}

}









































