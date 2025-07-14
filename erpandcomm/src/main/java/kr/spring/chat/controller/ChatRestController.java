package kr.spring.chat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.spring.chat.service.ChatService;
import kr.spring.chat.vo.ChatRoomVO;
import kr.spring.member.vo.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.chat.vo.ChatMessageVO;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatRestController {
	
	@Autowired
	private ChatService chatService;
	
    // 방 입장
    @GetMapping("/enter/{room_num}")
    public ResponseEntity<Map<String, Object>> enterRoom(@PathVariable("room_num") long room_num, 
            @AuthenticationPrincipal PrincipalDetails principal) {
        
        Map<String, Object> mapAjax = new HashMap<>();
        
        try {
            log.debug("<<채팅방 입장 요청>> room_num: {}", room_num);
            
            long userNum = principal.getMemberVO().getUser_num();
            String userName = principal.getMemberVO().getUser_name();
            
            log.debug("<<로그인 사용자 정보>> userNum: {}, userName: {}", userNum, userName);

            // 채팅방 정보 조회
            Map<String, Object> roomMap = new HashMap<String, Object>();
            roomMap.put("room_num", room_num);
            roomMap.put("member_num", userNum);
            List<ChatRoomVO> roomList = chatService.selectListChatRoom(roomMap);
            
            log.debug("<<채팅방 정보 조회 결과>> roomList: {}", roomList);

            if (roomList.isEmpty()) {
                mapAjax.put("result", "error");
                mapAjax.put("message", "채팅방을 찾을 수 없습니다.");
                return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.NOT_FOUND);
            }

            ChatRoomVO room = roomList.get(0);

            // 채팅방 멤버 리스트 조회
            Map<String, Object> memberMap = new HashMap<String, Object>();
            memberMap.put("room_num", room_num);
            List<ChatMemberVO> memberList = chatService.selectMember(memberMap);
            
            log.debug("<<채팅방 멤버 조회 결과>> memberList: {}", memberList);

            // 로그인한 사용자가 멤버인지 확인
            boolean isMember = false;
            boolean isFirstJoin = true;
            for (ChatMemberVO member : memberList) {
                if (member.getUser_num() == userNum) {
                    isMember = true;
                    isFirstJoin = false;
                    break;
                }
            }

            if (!isMember) {
                mapAjax.put("result", "error");
                mapAjax.put("message", "채팅방 멤버가 아닙니다.");
                return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.FORBIDDEN);
            }

            // 메시지 조회
            List<ChatMessageVO> messageList = chatService.selectMessage(room_num);
            
            log.debug("<<채팅 메시지 조회 결과>> messageList size: {}", messageList != null ? messageList.size() : 0);

            // 처음 입장하는 경우 입장 메시지 추가
            if (isFirstJoin) {
                ChatMessageVO joinMessage = new ChatMessageVO();
                joinMessage.setRoom_num(room_num);
                joinMessage.setSender_num(userNum);
                joinMessage.setContent(userName + "님이 채팅방에 입장하셨습니다.");
                chatService.insertMessage(joinMessage);
                
                log.debug("<<입장 메시지 추가>> message: {}", joinMessage);
                
                // 새로 추가된 입장 메시지도 포함하여 다시 조회
                messageList = chatService.selectMessage(room_num);
            }

            mapAjax.put("result", "success");
            mapAjax.put("room", room);
            mapAjax.put("memberList", memberList);
            mapAjax.put("messageList", messageList);
            mapAjax.put("currentUserNum", userNum);

            return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("<<채팅방 입장 처리 중 오류 발생>> room_num: " + room_num, e);
            mapAjax.put("result", "error");
            mapAjax.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 메시지 전송
    @PostMapping("/messages/send")
    public ResponseEntity<Map<String, Object>> sendMessage(
    		@RequestBody ChatMessageVO message,
    		@AuthenticationPrincipal PrincipalDetails principal) {
    	
    	Map<String, Object> mapAjax = new HashMap<>();
    	
    	try {
    		long userNum = principal.getMemberVO().getUser_num();
    		String userName = principal.getMemberVO().getUser_name();
    		
    		message.setSender_num(userNum);
    		
    		// 메시지 저장
    		chatService.insertMessage(message);
    		
    		// 저장된 메시지 정보에 보낸 사람 이름 추가
    		message.setSender_name(userName);
    		
    		mapAjax.put("result", "success");
    		mapAjax.put("message", message);
    		
    		return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.OK);
    		
    	} catch (Exception e) {
    		log.error("메시지 전송 중 오류 발생", e);
    		mapAjax.put("result", "error");
    		mapAjax.put("message", "메시지 전송에 실패했습니다.");
    		return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
    
    // 새 메시지 조회
    @GetMapping("/messages/{room_num}")
    public ResponseEntity<Map<String, Object>> getNewMessages(
            @PathVariable("room_num") long room_num,
            @RequestParam(value = "lastMessageId", defaultValue = "0") long lastMessageId) {
        
        Map<String, Object> mapAjax = new HashMap<>();
        
        try {
            // lastMessageId 이후의 새 메시지만 조회
            List<ChatMessageVO> messages = chatService.selectNewMessages(room_num, lastMessageId);
            
            mapAjax.put("result", "success");
            mapAjax.put("messages", messages);
            
            return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("메시지 조회 중 오류 발생", e);
            mapAjax.put("result", "error");
            mapAjax.put("message", "메시지 조회에 실패했습니다.");
            return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}









































