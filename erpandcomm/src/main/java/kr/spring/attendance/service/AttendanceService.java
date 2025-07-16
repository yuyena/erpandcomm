package kr.spring.attendance.service;
import java.util.Map;
import kr.spring.attendance.vo.AttendanceVO;
import kr.spring.member.vo.MemberVO;

public interface AttendanceService {
	public void insertAttendance(AttendanceVO attendence);
	public AttendanceVO selectAttendance(Long empId);
	public MemberVO selectList(Map<String,Object> map);
	public Integer selectRowCount(Map<String,Object> map);
	public void updateAttendance(AttendanceVO attendence);
	public void deleteAttendance(Long empId);
}
