package kr.spring.member.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.member.dao.MemberMapper;
import kr.spring.member.vo.MemberVO;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public void insertMember(MemberVO member) {
		//member.setMem_num(memberMapper.selectMemNum());
		memberMapper.insertMember(member);
		memberMapper.insertMemberDetail(member);
	}


	@Override
	public MemberVO selectCheckMember(String employee_code) {
		return memberMapper.selectCheckMember(employee_code);
	}

	@Override
	public MemberVO selectMember(Long user_num) {
		return memberMapper.selectMember(user_num);
	}

	@Override
	public void updateMember(MemberVO member) {
		memberMapper.updateMemberDetail(member);
	}

	@Override
	public void updatePassword(MemberVO member) {
		memberMapper.updatePassword(member);
		// 설정되어 있는 자동 로그인 해제(모든 브라우저에 설정된 자동로그인 해제)
//		memberMapper.deleteRememberMe(member);
	}

	@Override
	public void deleteMember(Long mem_num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRandomPassword(MemberVO member) {
		memberMapper.updateRandomPassword(member);
	}

	@Override
	public void updateProfile(MemberVO member) {
		memberMapper.updateProfile(member);
	}

	@Override
	public Integer selectRowCount(Map<String, Object> map) {
		return memberMapper.selectRowCount(map);
	}

	@Override
	public List<MemberVO> selectList(Map<String, Object> map) {
		return memberMapper.selectList(map);
	}

	@Override
	public void updateByAdmin(MemberVO memberVO) {
		memberMapper.updateByAdmin(memberVO);
	}


	@Override
	public List<MemberVO> selectMemberList(Map<String, Object> map) {
		return memberMapper.selectMemberList(map);
	}

}













































