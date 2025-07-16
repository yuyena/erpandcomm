package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.chat.vo.ChatMessageReadVO;

@Mapper
public interface ChatMessageReadMapper {
	
	public List<ChatMessageReadVO> selectMessageRead(long message_num);
	public void insertMessageRead(ChatMessageReadVO messageRead);
	public void insertMessageReadBatch(Map<String, Object> map);
	public int countUnreadMessage(Map<String, Object> map);
	
	// 퇴사 시 삭제
	public void deleteMessageReadByUserNum(long user_num);
	public void deleteMessageReadByRoomNum(long room_num);

}
