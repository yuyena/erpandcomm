package kr.spring.attendance.service;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.attendance.dao.AttendanceMapper;
import kr.spring.attendance.vo.AttendanceVO;
import kr.spring.member.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
	
	@Autowired
	private AttendanceMapper attendanceMapper;
	
	@Override
	public void insertAttendance(AttendanceVO attendance) {
		attendanceMapper.insertAttendance(attendance);
		
	}
	// 개별
	@Override
	public AttendanceVO selectAttendance(Long empId) {
		return attendanceMapper.selectAttendance(empId);
	}

	
	@Override
	public Integer selectRowCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAttendance(AttendanceVO attendance) {
		// TODO Auto-generated method stub
		attendanceMapper.updateAttendance(attendance);
	}

	@Override
	public void deleteAttendance(Long empId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MemberVO selectList(Map<String, Object> map) {
		return attendanceMapper.selectList(map);
	}
	
	// 여러개
	@Override
	public List<AttendanceVO> selectAttedanceList(Long empId) {
		return attendanceMapper.selectAttendanceList(empId);
	}
}
