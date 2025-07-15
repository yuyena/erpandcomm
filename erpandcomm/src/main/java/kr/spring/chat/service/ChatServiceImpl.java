package kr.spring.chat.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.chat.dao.ChatMemberMapper;
import kr.spring.chat.dao.ChatMessageMapper;
import kr.spring.chat.dao.ChatMessageReadMapper;
import kr.spring.chat.dao.ChatRoomMapper;
import kr.spring.chat.vo.ChatMemberVO;
import kr.spring.chat.vo.ChatMessageReadVO;
import kr.spring.chat.vo.ChatMessageVO;
import kr.spring.chat.vo.ChatRoomVO;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	private ChatRoomMapper chatRoomMapper;
	@Autowired
	private ChatMemberMapper chatMemberMapper;
	@Autowired
	private ChatMessageMapper chatMessageMapper;
	@Autowired
	private ChatMessageReadMapper chatMessageReadMapper;
	
	@Override
	public List<ChatRoomVO> selectListChatRoom(Map<String, Object> map) {
		return chatRoomMapper.selectListChatRoom(map);
	}

	@Override
	public Integer selectRowCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertRoom(ChatRoomVO chatRoom) {
		chatRoomMapper.insertRoom(chatRoom);
	}

	@Override
	public long selectLastRoomNum() {
		return chatRoomMapper.selectLastRoomNum();
	}

	@Override
	public void updateRoom(ChatRoomVO chatRoom) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRoom(Long room_num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ChatMemberVO> selectMember(Map<String, Object> map) {
		return chatMemberMapper.selectMember(map);
	}
	
	@Override
	public void notActive(Long room_num) {
		chatRoomMapper.notActive(room_num);
	}

	@Override
	public void insertMember(ChatMemberVO chatMember) {
		chatMemberMapper.insertMember(chatMember);
	}

	@Override
	public long selectLastMemberNum() {
		return chatMemberMapper.selectLastMemberNum();
	}

	@Override
	public void updateMember(ChatMemberVO chatMember) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMember(ChatMemberVO chatMember) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ChatMessageVO> selectMessage(Long chat_room) {
		return chatMessageMapper.selectMessage(chat_room);
	}

	@Override
	public void insertMessage(ChatMessageVO chatMessage) {
		chatMessageMapper.insertMessage(chatMessage);
	}
	
	@Override
	public List<ChatMessageVO> selectNewMessages(Long room_num, Long lastMessageId) {
		Map<String, Object> map = new HashMap<>();
		map.put("room_num", room_num);
		map.put("lastMessageId", lastMessageId);
		return chatMessageMapper.selectNewMessages(map);
	}

	@Override
	public List<ChatMessageReadVO> selectMessageRead(Long message_num) {
		return chatMessageReadMapper.selectMessageRead(message_num);
	}

	@Override
	public void insertMessageRead(ChatMessageReadVO messageRead) {
		chatMessageReadMapper.insertMessageRead(messageRead);
	}

	@Override
	public int countUnreadMessage(Long message_num) {
		// 이 메서드는 더 이상 사용하지 않음 (쿼리에서 직접 계산)
		return 0;
	}
	
	// 채팅방 입장 시 모든 메시지를 읽음 처리
	public void markAllMessagesAsRead(Long room_num, Long user_num) {
		Map<String, Object> map = new HashMap<>();
		map.put("room_num", room_num);
		map.put("user_num", user_num);
		chatMessageReadMapper.insertMessageReadBatch(map);
	}
	
	// 특정 메시지를 읽음 처리
	public void markMessageAsRead(Long message_num, Long user_num) {
		ChatMessageReadVO messageRead = new ChatMessageReadVO();
		messageRead.setMessage_num(message_num);
		messageRead.setUser_num(user_num);
		chatMessageReadMapper.insertMessageRead(messageRead);
	}


}
