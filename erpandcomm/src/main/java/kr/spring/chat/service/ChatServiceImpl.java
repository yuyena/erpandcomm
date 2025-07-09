package kr.spring.chat.service;

import java.util.List;
import java.util.Map;

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
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertMessage(ChatMessageVO chatMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ChatMessageReadVO> selectMessageRead(Long message_num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertMessageRead(ChatMessageReadVO messageRead) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int countUnreadMessage(Long message_num) {
		// TODO Auto-generated method stub
		return 0;
	}


}
