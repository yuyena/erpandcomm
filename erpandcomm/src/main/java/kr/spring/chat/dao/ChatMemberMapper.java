package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.spring.chat.vo.ChatMemberVO;

@Mapper
public interface ChatMemberMapper {
	
	public List<ChatMemberVO> selectMember(Map<String, Object> map);
	public void insertMember(ChatMemberVO chatMember);
	@Select("SELECT chat_member_seq.currval FROM dual")
	public long selectLastMemberNum();
	public void updateMember(ChatMemberVO chatMember);
	public void deleteMember(ChatMemberVO chatMember);
	
	// 퇴사 시 삭제
	public void deleteMemberByUserNum(long user_num);
	public void deleteMemberByRoomNum(long room_num);

}
