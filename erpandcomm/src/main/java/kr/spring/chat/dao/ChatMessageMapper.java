package kr.spring.chat.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.chat.vo.ChatMessageVO;

@Mapper
public interface ChatMessageMapper {
	
	public List<ChatMessageVO> selectMessage(Long chat_room);
	public void insertMessage(ChatMessageVO chatMessage);

}
