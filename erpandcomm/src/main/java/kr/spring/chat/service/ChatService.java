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
	public void deleteRoom(long room_num);
	// 채팅방 비활성화
	public void notActive(long room_num);
	
	// ChatMember
	public List<ChatMemberVO> selectMember(Map<String, Object> map);
	public void insertMember(ChatMemberVO chatMember);
	public long selectLastMemberNum();
	public void updateMember(ChatMemberVO chatMember);
	public void deleteMember(ChatMemberVO chatMember);
	
	// ChatMessage
	public List<ChatMessageVO> selectMessage(long room_num, long current_user_num);
	public void insertMessage(ChatMessageVO chatMessage);
	public List<ChatMessageVO> selectNewMessages(long room_num, long lastMessageId);
	
	// ChatMessageRead
	public List<ChatMessageReadVO> selectMessageRead(long message_num);
	public void markMessageAsRead(long message_num, long user_num);
	public int countUnreadMessage(Map<String, Object> map);
	
	// 메시지 읽음 처리
	public void markAllMessagesAsRead(long room_num, long user_num);

}
