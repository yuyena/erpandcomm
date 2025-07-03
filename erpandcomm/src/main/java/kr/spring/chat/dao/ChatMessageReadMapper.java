package kr.spring.chat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.chat.vo.ChatMessageReadVO;

@Mapper
public interface ChatMessageReadMapper {
	
	public List<ChatMessageReadVO> selectMessageRead(Long message_num);
	public void insertMessageRead(ChatMessageReadVO messageRead);
	public int countUnreadMessage(Long message_num);
	
	// 퇴사 시 삭제
	public void deleteMessageReadByUserNum(Long user_num);
	public void deleteMessageReadByRoomNum(Long room_num);

}
