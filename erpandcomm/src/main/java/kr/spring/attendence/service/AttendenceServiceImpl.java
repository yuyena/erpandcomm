package kr.spring.attendence.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.attendence.dao.AttendenceMapper;
import kr.spring.attendence.vo.AttendenceVO;

@Service
@Transactional
public class AttendenceServiceImpl implements AttendenceService {
	@Autowired
	private AttendenceMapper attendenceMapper;
	@Override
	public void insertAttendence(AttendenceVO attendence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AttendenceVO selectAttendence(Long empId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AttendenceVO> selectList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectRowCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAttendence(AttendenceVO attendence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAttendence(Long empId) {
		// TODO Auto-generated method stub
		
	}

}
