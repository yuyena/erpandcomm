package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.chat.vo.ChatMessageVO;

@Mapper
public interface ChatMessageMapper {
	
	public List<ChatMessageVO> selectMessage(Map<String, Object> map);
	public void insertMessage(ChatMessageVO chatMessage);
	public List<ChatMessageVO> selectNewMessages(Map<String, Object> map);
	
	// 퇴사 시 삭제
	public void deleteMessageByUserNum(Long user_num);
	public void deleteMessageByRoomNum(Long room_num);

}
