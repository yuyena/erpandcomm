package kr.spring.chat.service;

import java.util.List;
import java.util.Map;

import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.chat.vo.ChatMessageReadVO;
import kr.spring.chat.vo.ChatMessageVO;
import kr.spring.chat.vo.ChatRoomVO;

public interface ChatService {
	
	// ChatRoom
	public List<ChatRoomVO> selectListChatRoom(Long user_num);
	public Integer selectRowCount(Map<String, Object> map);
	public void insertRoom(ChatRoomVO chatRoom);
	public void updateRoom(ChatRoomVO chatRoom);
	public void deleteRoom(Long room_num);
	
	// ChatMember
	public List<ChatMemberVO> selectMember(Map<String, Object> map);
	public void insertMember(ChatMemberVO chatMember);
	public void updateMember(ChatMemberVO chatMember);
	public void deleteMember(ChatMemberVO chatMember);
	
	// ChatMessage
	public List<ChatMessageVO> selectMessage(Long chat_room);
	public void insertMessage(ChatMessageVO chatMessage);
	
	// ChatMessageRead
	public List<ChatMessageReadVO> selectMessageRead(Long message_num);
	public void insertMessageRead(ChatMessageReadVO messageRead);
	public int countUnreadMessage(Long message_num);

}
