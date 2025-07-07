package kr.spring.chat.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import kr.spring.chat.service.ChatService;
import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.chat.vo.ChatMessageReadVO;
import kr.spring.chat.vo.ChatMessageVO;
import kr.spring.chat.vo.ChatRoomVO;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.util.ValidationUtil;
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
	public String form(Model model) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<MemberVO> userList = memberService.selectMemberList(map);
		
	    // 임시로 1번 사용자를 본인으로 간주
	    long user_num = 1;

	    List<MemberVO> filteredList = new ArrayList<>();
	    for (MemberVO member : userList) {
	        if (member.getUser_num() != user_num) {
	            filteredList.add(member);
	        }
	    }
		
		model.addAttribute("userList", filteredList);
		return "views/chat/createRoom";
	}
	
	// 채팅방 생성
	@PostMapping("/createRoom")
	public String submit(@Valid ChatRoomVO chatRoomVO, @RequestParam(value="memberNums", required=false) List<Long> memberNums,
						 BindingResult result, Model model, HttpServletRequest request) {
		
		if (result.hasErrors()) {
			ValidationUtil.printErrorFields(result);
			return form(model);
		} // if
		
		chatService.insertRoom(chatRoomVO);
		long roomNum = chatRoomVO.getRoom_num(); // 생성된 방 번호 (insertRoom에서 세팅되어야 함)

		// 선택된 멤버 추가
		if (memberNums != null && !memberNums.isEmpty()) {
		    for (long userNum : memberNums) {
		        ChatMemberVO chatMemberVO = new ChatMemberVO();
		        chatMemberVO.setRoom_num(roomNum);
		        chatMemberVO.setUser_num(userNum);
		        chatService.insertMember(chatMemberVO);
		    }
		}
		
		model.addAttribute("accessMsg", "채팅방이 생성되었습니다.");
		model.addAttribute("accessUrl", request.getContextPath()+"/chat/roomList");
		
		return "views/common/resultView";
	}
	
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
	    
	    log.debug("<<회원 목록>> : " + userList);
		
		return "views/chat/chatRoomList";
	}

}









































