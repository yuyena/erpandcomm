package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.chat.vo.ChatRoomVO;

@Mapper
public interface ChatRoomMapper {
	
	public void insertRoom(ChatRoomVO chatRoom);
	public long selectLastRoomNum();
	public List<ChatRoomVO> selectListChatRoom(Map<String, Object> map);
	
}








































