package kr.spring.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.member.vo.MemberVO;

@Mapper
public interface MemberMapper {
	
	// 회원관리 - 일반회원
	@Select("SELECT spmember_seq.nextval FROM dual")
	public Long selectMemNum();
	@Insert("INSERT INTO spmember (mem_num,id,nick_name) VALUES (#{mem_num},#{id},#{nick_name})")
	public void insertMember(MemberVO member);
	public void insertMemberDetail(MemberVO member);
	public MemberVO selectCheckMember(String employee_code);
	@Select("SELECT * FROM spmember JOIN spmember_detail USING(mem_num) WHERE mem_num=#{mem_num}")
	public MemberVO selectMember(Long mem_num);
	@Update("UPDATE spmember SET nick_name=#{nick_name} WHERE mem_num=#{mem_num}")
	public void updateMember(MemberVO member);
	public void updateMemberDetail(MemberVO member);
	@Update("UPDATE euser_detail SET passwd=#{passwd} WHERE user_num=#{user_num}")
	public void updatePassword(MemberVO member);
	public void deleteMember(Long mem_num);
	public void deleteMemberDetail(Long mem_num);
	
	// 채팅방 멤버 추가용 회원 목록 조회
	public List<MemberVO> selectMemberList(Map<String, Object> map);
	
	// 자동 로그인 해제
	@Delete("DELETE FROM persistent_logins WHERE username=#{id}")
	public void deleteRememberMe(MemberVO member);
	
	// 비밀번호 찾기
	@Update("UPDATE spmember_detail SET passwd=#{passwd} WHERE mem_num=#{mem_num}")
	public void updateRandomPassword(MemberVO member);
	
	// 프로필 이미지 업데이트
	@Update("UPDATE spmember_detail SET photo=#{photo},photo_name=#{photo_name} WHERE mem_num=#{mem_num}")
	public void updateProfile(MemberVO member);
	
	// 회원관리 - 관리자
	public Integer selectRowCount(Map<String, Object> map);
	public List<MemberVO> selectList(Map<String, Object> map);
	@Update("UPDATE spmember SET authority=#{authority} WHERE mem_num=#{mem_num}")
	public void updateByAdmin(MemberVO memberVO);

}













































