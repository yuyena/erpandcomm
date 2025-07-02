package kr.spring.chat.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.chat.vo.ChatMemberVO;

@Mapper
public interface ChatMemberMapper {
	
	public List<ChatMemberVO> selectMember(Map<String, Object> map);
	public void insertMember(ChatMemberVO chatMember);
	public void updateMember(ChatMemberVO chatMember);
	public void deleteMember(ChatMemberVO chatMember);

}
