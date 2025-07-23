package kr.spring.attendance.service;
import java.util.List;
import java.util.Map;
import kr.spring.attendance.vo.AttendanceVO;
import kr.spring.attendance.vo.SearchVO;
import kr.spring.member.vo.MemberVO;

public interface AttendanceService {
	public void insertAttendance(AttendanceVO attendance);
	public AttendanceVO selectAttendance(Long empId);
	public List<AttendanceVO> selectAttedanceList(Long empId);
	public List<AttendanceVO> AttendanceSelectList(SearchVO searchVO);
	public MemberVO selectList(Map<String,Object> map);
	public Integer selectRowCount(Map<String,Object> map);
	public void updateAttendance(AttendanceVO attendance);
	public void deleteAttendance(Long empId);
}
