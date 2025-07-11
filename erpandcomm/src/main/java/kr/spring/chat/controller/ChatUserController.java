package kr.spring.chat.controller;

import java.util.ArrayList;
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
import kr.spring.chat.service.ChatService;
import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.chat.vo.ChatMessageReadVO;
import kr.spring.chat.vo.ChatMessageVO;
import kr.spring.chat.vo.ChatRoomVO;
import kr.spring.member.service.MemberService;
import kr.spring.member.vo.MemberVO;
import kr.spring.member.vo.PrincipalDetails;
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
	@PreAuthorize("isAuthenticated()")
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
		model.addAttribute("chatRoomVO", new ChatRoomVO()); // 바인딩 객체 추가
		return "views/chat/createRoom";
	}
	
	// 채팅방 생성
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/createRoom")
	public String submit(@Valid ChatRoomVO chatRoomVO, @RequestParam(value="memberNums", required=false) List<Long> memberNums,
						 BindingResult result, Model model, HttpServletRequest request) {
		
		if (result.hasErrors()) {
			ValidationUtil.printErrorFields(result);
			return form(model);
		} // if
		
		// 디버깅 로그
		log.debug("<<채팅방 생성 요청>>");
		log.debug("방 이름: " + chatRoomVO.getRoom_name());
		log.debug("설명: " + chatRoomVO.getDescription());
		log.debug("최대 인원: " + chatRoomVO.getMax_members());
		log.debug("선택된 멤버: " + memberNums);
		
		// 임시로 1번 사용자를 생성자로 설정
		long creatorUserNum = 1L;
		chatRoomVO.setCreated_by(creatorUserNum);
		
		// 방 이름이 없으면 자동 생성
		if (chatRoomVO.getRoom_name() == null || chatRoomVO.getRoom_name().trim().isEmpty()) {
			// 생성자 정보 가져오기
			Map<String, Object> map = new HashMap<String, Object>();
			List<MemberVO> userList = memberService.selectMemberList(map);
			
			String creatorName = "사용자";
			for (MemberVO member : userList) {
				if (member.getUser_num() == creatorUserNum) {
					creatorName = member.getUser_name();
					break;
				}
			}
			chatRoomVO.setRoom_name(creatorName + "의 방");
		}
		
		// 본인(생성자)까지 포함해서 2명이면 1:1, 3명 이상이면 group
		int totalMembers = (memberNums != null ? memberNums.size() : 0) + 1; // +1은 본인
		log.debug("총 멤버 수: " + totalMembers);
		
		if (totalMembers == 2) {
			chatRoomVO.setRoom_type("1:1");
		} else {
			chatRoomVO.setRoom_type("group");
		}
		
		chatService.insertRoom(chatRoomVO);
		long room_num = chatService.selectLastRoomNum(); // 생성된 방 번호 조회
		log.debug("생성된 방 번호: " + room_num);

	    // 본인(생성자)도 멤버로 추가 (방장 역할)
	    ChatMemberVO creatorMember = new ChatMemberVO();
	    creatorMember.setRoom_num(room_num);
	    creatorMember.setUser_num(creatorUserNum);
	    creatorMember.setRole("방장");
	    chatService.insertMember(creatorMember);
	    log.debug("방장 추가 완료: " + creatorUserNum);

		// 선택된 멤버 추가
	    if (memberNums != null && !memberNums.isEmpty()) {
	        for (long user_num : memberNums) {
	            ChatMemberVO chatMemberVO = new ChatMemberVO();
	            chatMemberVO.setRoom_num(room_num);
	            chatMemberVO.setUser_num(user_num);
	            chatMemberVO.setRole("멤버");
	            chatService.insertMember(chatMemberVO);
	            log.debug("멤버 추가 완료: " + user_num);
	        }
	    } else {
	    	log.debug("선택된 멤버가 없습니다.");
	    }
		
		model.addAttribute("accessMsg", "채팅방이 생성되었습니다.");
		model.addAttribute("accessUrl", request.getContextPath()+"/chat/roomList");
		
		return "views/common/resultView";
	}
	
	// 채팅방 목록
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/roomList")
	public String getRoomList(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
		
	    // euser 테이블에서 사용자 목록 조회
	    Map<String, Object> listMap = new HashMap<String, Object>();
	    List<MemberVO> userList = memberService.selectMemberList(listMap);
		
	    // 로그인한 사용자의 user_num 가져오기
	    long user_num = principal.getMemberVO().getUser_num();
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("member_num", user_num);

	    // 서비스에서 채팅방 목록 조회
	    List<ChatRoomVO> chatRoomList = chatService.selectListChatRoom(map);

	    // 모델에 담기
	    model.addAttribute("userList", userList);
	    model.addAttribute("accessBtn", "목록으로");
	    model.addAttribute("chatRoomList", chatRoomList);
	    
//	    log.debug("<<회원 목록>> : " + userList);
	    log.debug("<<현재 로그인한 사용자 번호>> : " + user_num);
		
		return "views/chat/chatRoomList";
	}

	// 채팅방 삭제(비활성화)
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/deleteRoom")
	public String deleteRoom(@AuthenticationPrincipal PrincipalDetails principal, long room_num, HttpServletRequest request, Model model) {
	    long myUserNum = 1; // 테스트용으로 항상 1로 고정

	    // 1. 전체 멤버 조회
	    Map<String, Object> param = new HashMap<>();
	    param.put("room_num", room_num);

	    List<ChatMemberVO> memberList = chatService.selectMember(param);

	    if (memberList == null) memberList = new java.util.ArrayList<>();

	    // 2. 방장 찾기
	    ChatMemberVO owner = null;
	    for (ChatMemberVO member : memberList) {
	        if (member.getRole().equals("방장")) {
	            owner = member;
	            break;
	        }
	    }
	    
	    log.debug("<<memberList>> : " + memberList);
	    log.debug("<<owner>> : " + owner);
	    log.debug("<<userNum>> : " + myUserNum);
	    log.debug("<<roomNum>> : " + room_num);

	    // 3. 권한 체크 및 삭제(비활성화)
	    if (owner != null && myUserNum == owner.getUser_num()) {
	        chatService.notActive(room_num);
	        model.addAttribute("accessMsg", "채팅방이 삭제(비활성화)되었습니다.");
	    } else {
	        model.addAttribute("accessMsg", "방장만 삭제할 수 있습니다.");
	    }
	    model.addAttribute("accessUrl", request.getContextPath() + "/chat/roomList");
	    return "views/common/resultView";
	}

}









































