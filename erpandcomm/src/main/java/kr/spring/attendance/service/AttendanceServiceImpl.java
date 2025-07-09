package kr.spring.attendance.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.attendance.dao.AttendanceMapper;
import kr.spring.attendance.vo.AttendanceVO;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

	@Override
	public void insertAttendance(AttendanceVO attendence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AttendanceVO selectAttendance(Long empId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttendanceVO> selectList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectRowCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAttendance(AttendanceVO attendence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAttendance(Long empId) {
		// TODO Auto-generated method stub
		
	}

	
	
	

}
