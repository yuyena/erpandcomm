package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import kr.spring.chat.vo.ChatRoomVO;

@Mapper
public interface ChatRoomMapper {
	
	public void insertRoom(ChatRoomVO chatRoom);
	public long selectLastRoomNum();
	public List<ChatRoomVO> selectListChatRoom(Map<String, Object> map);
	
	// 채팅방 비활성화
	@Update("UPDATE chat_room SET is_active = 'N' WHERE room_num=#{room_num}")
	public void notActive(long room_num);
	
}








































