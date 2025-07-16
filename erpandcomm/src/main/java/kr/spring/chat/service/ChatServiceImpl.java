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
	public void deleteRoom(long room_num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ChatMemberVO> selectMember(Map<String, Object> map) {
		return chatMemberMapper.selectMember(map);
	}
	
	@Override
	public void notActive(long room_num) {
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
	public List<ChatMessageVO> selectMessage(long room_num, long current_user_num) {
		Map<String, Object> map = new HashMap<>();
		map.put("room_num", room_num);
		map.put("current_user_num", current_user_num);
		return chatMessageMapper.selectMessage(map);
	}

	@Override
	public void insertMessage(ChatMessageVO chatMessage) {
		chatMessageMapper.insertMessage(chatMessage);
	}
	
	@Override
	public List<ChatMessageVO> selectNewMessages(long room_num, long lastMessageId) {
		Map<String, Object> map = new HashMap<>();
		map.put("room_num", room_num);
		map.put("lastMessageId", lastMessageId);
		map.put("current_user_num", 0L); // 임시로 0 설정 (필요시 파라미터 추가)
		return chatMessageMapper.selectNewMessages(map);
	}

	@Override
	public List<ChatMessageReadVO> selectMessageRead(long message_num) {
		return chatMessageReadMapper.selectMessageRead(message_num);
	}

	@Override
	public int countUnreadMessage(Map<String, Object> map) {
		return chatMessageReadMapper.countUnreadMessage(map);
	}

	@Override
	public void markMessageAsRead(long message_num, long user_num) {
		// 이미 읽음 처리된 메시지인지 확인
		List<ChatMessageReadVO> readList = chatMessageReadMapper.selectMessageRead(message_num);
		boolean alreadyRead = false;
		
		for (ChatMessageReadVO read : readList) {
			if (read.getUser_num() == user_num) {
				alreadyRead = true;
				break;
			}
		}
		
		// 아직 읽지 않은 메시지인 경우에만 읽음 처리
		if (!alreadyRead) {
			ChatMessageReadVO messageRead = new ChatMessageReadVO();
			messageRead.setMessage_num(message_num);
			messageRead.setUser_num(user_num);
			chatMessageReadMapper.insertMessageRead(messageRead);
		}
	}

	// 채팅방 입장 시 모든 메시지를 읽음 처리
	public void markAllMessagesAsRead(long room_num, long user_num) {
		Map<String, Object> map = new HashMap<>();
		map.put("room_num", room_num);
		map.put("user_num", user_num);
		
		try {
			// 배치로 모든 메시지 읽음 처리 (더 효율적)
			chatMessageReadMapper.insertMessageReadBatch(map);
		} catch (Exception e) {
			// 로그 출력 후 계속 진행 (읽음 처리 실패해도 채팅방 입장은 가능해야 함)
			System.err.println("메시지 읽음 처리 중 오류 발생: " + e.getMessage());
		}
	}


}
