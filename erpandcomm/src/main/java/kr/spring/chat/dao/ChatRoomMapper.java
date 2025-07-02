package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.chat.vo.ChatRoomVO;

@Mapper
public interface ChatRoomMapper {
	
	public List<ChatRoomVO> selectListChatRoom(Map<String, Object> map);
	public Integer selectRowCount(Map<String, Object> map);
	public void insertRoom(ChatRoomVO chatRoom);
	public void updateRoom(ChatRoomVO chatRoom);
	public void deleteRoom(Long room_num);

}








































