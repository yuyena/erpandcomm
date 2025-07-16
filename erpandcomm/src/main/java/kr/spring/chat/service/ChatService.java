package kr.spring.chat.service;

import java.util.List;
import java.util.Map;

import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.chat.vo.ChatMessageReadVO;
import kr.spring.chat.vo.ChatMessageVO;
import kr.spring.chat.vo.ChatRoomVO;

public interface ChatService {
	
	// ChatRoom
	public List<ChatRoomVO> selectListChatRoom(Map<String, Object> map);
	public Integer selectRowCount(Map<String, Object> map);
	public void insertRoom(ChatRoomVO chatRoom);
	public long selectLastRoomNum();
	public void updateRoom(ChatRoomVO chatRoom);
	public void deleteRoom(Long room_num);
	// 채팅방 비활성화
	public void notActive(Long room_num);
	
	// ChatMember
	public List<ChatMemberVO> selectMember(Map<String, Object> map);
	public void insertMember(ChatMemberVO chatMember);
	public long selectLastMemberNum();
	public void updateMember(ChatMemberVO chatMember);
	public void deleteMember(ChatMemberVO chatMember);
	
	// ChatMessage
	public List<ChatMessageVO> selectMessage(Long room_num, Long current_user_num);
	public void insertMessage(ChatMessageVO chatMessage);
	public List<ChatMessageVO> selectNewMessages(Long room_num, Long lastMessageId);
	
	// ChatMessageRead
	public List<ChatMessageReadVO> selectMessageRead(Long message_num);
	public void markMessageAsRead(Long message_num, Long user_num);
	public int countUnreadMessage(Map<String, Object> map);
	
	// 메시지 읽음 처리
	public void markAllMessagesAsRead(Long room_num, Long user_num);

}
