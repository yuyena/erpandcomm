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
import kr.spring.chat.vo.ChatMessageReadVO;
import kr.spring.websocket.SocketHandler;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatRestController {
	
	@Autowired
	private ChatService chatService;
	@Autowired
	private SocketHandler socketHandler;
	
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
            List<ChatMessageVO> messageList = chatService.selectMessage(room_num, userNum);
            
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
                messageList = chatService.selectMessage(room_num, userNum);
            }
            
            // 채팅방 입장 시 모든 메시지를 읽음 처리
            chatService.markAllMessagesAsRead(room_num, userNum);
            log.debug("<<모든 메시지 읽음 처리 완료>> room_num: {}, user_num: {}", room_num, userNum);

            // WebSocket을 통해 채팅방 입장 알림 전송
            StringBuilder enterMessageBuilder = new StringBuilder();
            enterMessageBuilder.append("{\"type\":\"ROOM_ENTERED\",");
            enterMessageBuilder.append("\"room_num\":").append(room_num).append(",");
            enterMessageBuilder.append("\"user_num\":").append(userNum).append(",");
            enterMessageBuilder.append("\"user_name\":\"").append(userName).append("\"}");
            String enterMessage = enterMessageBuilder.toString();
            socketHandler.broadcastMessage(enterMessage);
            log.debug("<<채팅방 입장 WebSocket 알림 전송>> {}", enterMessage);

            mapAjax.put("result", "success");
            mapAjax.put("room", room);
            mapAjax.put("memberList", memberList);
            mapAjax.put("messageList", messageList);
            mapAjax.put("currentUserNum", userNum);
            mapAjax.put("currentUserName", userName);

            return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("<<채팅방 입장 처리 중 오류 발생>> room_num: " + room_num, e);
            mapAjax.put("result", "error");
            mapAjax.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 특정 채팅방의 안읽은 메시지 존재 여부 확인
    @GetMapping("/checkUnreadMessages")
    public ResponseEntity<Map<String, Object>> checkUnreadMessages(
            @RequestParam(value = "room_num") long room_num,
            @AuthenticationPrincipal PrincipalDetails principal) {
        
        Map<String, Object> mapAjax = new HashMap<>();
        
        try {
            long userNum = principal.getMemberVO().getUser_num();
            
            // 채팅방 정보 조회 (해당 사용자가 멤버인지 확인)
            Map<String, Object> roomMap = new HashMap<>();
            roomMap.put("room_num", room_num);
            roomMap.put("member_num", userNum);
            List<ChatRoomVO> roomList = chatService.selectListChatRoom(roomMap);
            
            if (roomList.isEmpty()) {
                mapAjax.put("result", "error");
                mapAjax.put("message", "채팅방을 찾을 수 없습니다.");
                return new ResponseEntity<>(mapAjax, HttpStatus.NOT_FOUND);
            }
            
            // 안읽은 메시지 존재 여부 확인
            boolean hasUnreadMessages = roomList.get(0).isHasUnreadMessages();
            
            mapAjax.put("result", "success");
            mapAjax.put("hasUnreadMessages", hasUnreadMessages);
            
            return new ResponseEntity<>(mapAjax, HttpStatus.OK);
            
        } catch (Exception e) {
            log.error("안읽은 메시지 확인 중 오류 발생 - room_num: " + room_num, e);
            mapAjax.put("result", "error");
            mapAjax.put("message", "서버 오류가 발생했습니다.");
            return new ResponseEntity<>(mapAjax, HttpStatus.INTERNAL_SERVER_ERROR);
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
    		
    		// 저장된 메시지를 다시 조회하여 정확한 시간과 안 읽은 수 정보 가져오기
    		List<ChatMessageVO> messages = chatService.selectMessage(message.getRoom_num(), userNum);
    		ChatMessageVO savedMessage = null;
    		
    		// 가장 최근 메시지 찾기 (방금 저장한 메시지)
    		if (!messages.isEmpty()) {
    			savedMessage = messages.get(messages.size() - 1);
    			// sender_name 설정 (WebSocket에서 필요)
    			savedMessage.setSender_name(userName);
    		}
    		
    		// 저장된 메시지가 없으면 원본 메시지 사용하되 unread_count 직접 계산
    		if (savedMessage == null) {
    			savedMessage = message;
    			savedMessage.setSender_name(userName);
    			// DurationFromNow.getTimeDiffLabel()이 기대하는 형식으로 설정
    			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			savedMessage.setSent_at(sdf.format(new java.util.Date()));
    			
    			// 채팅방의 활성 멤버 수에서 본인을 제외한 수를 unread_count로 설정
    			Map<String, Object> memberParam = new HashMap<>();
    			memberParam.put("room_num", message.getRoom_num());
    			List<ChatMemberVO> memberList = chatService.selectMember(memberParam);
    			
    			int activeMemberCount = 0;
    			for (ChatMemberVO member : memberList) {
    				if ("Y".equals(member.getIs_active()) && member.getUser_num() != userNum) {
    					activeMemberCount++;
    				}
    			}
    			savedMessage.setUnread_count(activeMemberCount);
    		}
    		
    		mapAjax.put("result", "success");
    		mapAjax.put("message", savedMessage);
    		
    		// WebSocket을 통해 새 메시지 알림 전송
    		StringBuilder chatMessageBuilder = new StringBuilder();
    		chatMessageBuilder.append("{\"type\":\"CHAT\",");
    		chatMessageBuilder.append("\"room_num\":").append(message.getRoom_num()).append(",");
    		chatMessageBuilder.append("\"sender_num\":").append(userNum).append(",");
    		chatMessageBuilder.append("\"sender_name\":\"").append(userName).append("\",");
    		chatMessageBuilder.append("\"content\":\"").append(message.getContent().replace("\"", "\\\"")).append("\",");
    		chatMessageBuilder.append("\"message_num\":").append(savedMessage.getMessage_num()).append(",");
    		chatMessageBuilder.append("\"sent_at\":\"").append(savedMessage.getSent_at()).append("\",");
    		chatMessageBuilder.append("\"unread_count\":").append(savedMessage.getUnread_count()).append(",");
    		chatMessageBuilder.append("\"message_id\":\"msg_").append(savedMessage.getMessage_num()).append("\"}");
    		String chatMessage = chatMessageBuilder.toString();
    		socketHandler.broadcastMessage(chatMessage);
    		log.debug("<<새 메시지 WebSocket 알림 전송>> {}", chatMessage);
    		
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
    
    // 메시지 읽음 처리
    @PostMapping("/messages/{message_num}/read")
    public ResponseEntity<Map<String, Object>> markMessageAsRead(
    		@PathVariable("message_num") long message_num,
    		@RequestParam(value = "room_num", defaultValue = "0") long room_num,
    		@AuthenticationPrincipal PrincipalDetails principal) {
    	
    	Map<String, Object> mapAjax = new HashMap<>();
    	
    	try {
    		long userNum = principal.getMemberVO().getUser_num();
    		
    		// 메시지 읽음 처리
    		chatService.markMessageAsRead(message_num, userNum);
    		
    		// room_num이 제공된 경우에만 정확한 unread_count 계산
    		int updatedUnreadCount = 0;
    		if (room_num > 0) {
    			try {
    				// 1. 메시지 정보 조회 (메시지 전송자 확인을 위해)
    				List<ChatMessageVO> messageList = chatService.selectMessage(room_num, userNum);
    				long messageSenderNum = 0;
    				for (ChatMessageVO msg : messageList) {
    					if (msg.getMessage_num() == message_num) {
    						messageSenderNum = msg.getSender_num();
    						break;
    					}
    				}
    				
    				// 2. 채팅방의 활성 멤버 조회
    				Map<String, Object> memberMap = new HashMap<>();
    				memberMap.put("room_num", room_num);
    				List<ChatMemberVO> memberList = chatService.selectMember(memberMap);
    				
    				// 3. 활성 멤버 수 계산 (메시지 전송자 제외)
    				int activeMemberCount = 0;
    				for (ChatMemberVO member : memberList) {
    					if ("Y".equals(member.getIs_active()) && member.getUser_num() != messageSenderNum) {
    						activeMemberCount++;
    					}
    				}
    				
    				// 4. 해당 메시지를 읽은 사람 수 조회
    				List<ChatMessageReadVO> readList = chatService.selectMessageRead(message_num);
    				
    				// 5. 안읽은 사람 수 계산: 활성멤버(전송자 제외) - 읽은사람수
    				updatedUnreadCount = Math.max(0, activeMemberCount - readList.size());
    				
    				log.debug("<<안읽은 수 계산>> 활성멤버(전송자 제외): {}, 읽은사람: {}, 최종 안읽은 수: {}", 
    						activeMemberCount, readList.size(), updatedUnreadCount);
    				
    			} catch (Exception e) {
    				log.warn("멤버 조회 방식 안읽은 수 계산 중 오류 발생 (room_num: {}): {}", room_num, e.getMessage());
    				// 계산 실패해도 읽음 처리는 성공했으므로 계속 진행
    			}
    		}
    		
    		mapAjax.put("result", "success");
    		mapAjax.put("message_num", message_num);
    		mapAjax.put("unread_count", updatedUnreadCount);
    		
    		// WebSocket을 통해 메시지 읽음 상태 업데이트 알림 전송
    		if (room_num > 0) {
    			StringBuilder readMessageBuilder = new StringBuilder();
    			readMessageBuilder.append("{\"type\":\"READ\",");
    			readMessageBuilder.append("\"message_num\":").append(message_num).append(",");
    			readMessageBuilder.append("\"room_num\":").append(room_num).append(",");
    			readMessageBuilder.append("\"user_num\":").append(userNum).append(",");
    			readMessageBuilder.append("\"unread_count\":").append(updatedUnreadCount).append("}");
    			String readMessage = readMessageBuilder.toString();
    			socketHandler.broadcastMessage(readMessage);
    			log.debug("<<메시지 읽음 WebSocket 알림 전송>> {}", readMessage);
    		}
    		
    		return new ResponseEntity<>(mapAjax, HttpStatus.OK);
    		
    	} catch (Exception e) {
    		log.error("메시지 읽음 처리 중 오류 발생", e);
    		mapAjax.put("result", "error");
    		mapAjax.put("message", "메시지 읽음 처리에 실패했습니다.");
    		return new ResponseEntity<Map<String,Object>>(mapAjax, HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
}









































