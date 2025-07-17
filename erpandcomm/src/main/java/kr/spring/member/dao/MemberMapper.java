package kr.spring.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.member.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	public void insertMember(MemberVO member);
	public void insertMemberDetail(MemberVO member);
	public MemberVO selectCheckMember(String employee_code);
	@Select("SELECT * FROM euser JOIN euser_detail USING(user_num) JOIN department USING(department_num) WHERE user_num=#{user_num}")
	public MemberVO selectMember(Long mem_num);
	public void updateMember(MemberVO member);
	public void updateMemberDetail(MemberVO member);
	@Update("UPDATE euser_detail SET passwd=#{passwd} WHERE user_num=#{user_num}")
	public void updatePassword(MemberVO member);
	public void deleteRememberMe(MemberVO member);
	public void deleteMember(Long mem_num);
	public void updateRandomPassword(MemberVO member);
	public void updateProfile(MemberVO member);
	public Integer selectRowCount(Map<String, Object> map);
	public List<MemberVO> selectList(Map<String, Object> map);
	public void updateByAdmin(MemberVO memberVO);
	public List<MemberVO> selectMemberList(Map<String, Object> map);
	
}













































