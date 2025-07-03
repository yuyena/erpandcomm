package kr.spring.attendence.service;

import java.util.List;
import java.util.Map;

import kr.spring.attendence.vo.AttendenceVO;

public interface AttendenceService {
	public void insertAttendence(AttendenceVO attendence);
	public AttendenceVO selectAttendence(Long empId);
	public List<AttendenceVO> selectList(Map<String,Object> map);
	public Integer selectRowCount(Map<String,Object> map);
	public void updateAttendence(AttendenceVO attendence);
	public void deleteAttendence(Long empId);
}
