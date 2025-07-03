package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.spring.chat.vo.ChatRoomVO;

@Mapper
public interface ChatRoomMapper {
	
	@Select("SELECT * FROM chat_room WHERE user_num=#{user_num} ORDER BY created_at DESC")
	public List<ChatRoomVO> selectListChatRoom(Long user_num);
	public Integer selectRowCount(Map<String, Object> map);
	public void insertRoom(ChatRoomVO chatRoom);
	public void updateRoom(ChatRoomVO chatRoom);
	public void deleteRoom(Long room_num);

}








































