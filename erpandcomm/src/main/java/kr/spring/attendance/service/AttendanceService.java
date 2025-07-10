package kr.spring.attendance.service;

import java.util.List;
import java.util.Map;

import kr.spring.attendance.vo.AttendanceVO;

public interface AttendanceService {
	public void insertAttendance(AttendanceVO attendence);
	//중복체크
	boolean isDuplicate(AttendanceVO attendanceVO);
	public AttendanceVO selectAttendance(Long empId);
	public List<AttendanceVO> selectList(Map<String,Object> map);
	public Integer selectRowCount(Map<String,Object> map);
	public void updateAttendance(AttendanceVO attendence);
	public void deleteAttendance(Long empId);
}
