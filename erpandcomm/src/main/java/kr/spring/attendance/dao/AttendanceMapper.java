package kr.spring.attendance.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.spring.attendance.vo.AttendanceVO;
import kr.spring.member.vo.MemberVO;

@Mapper
public interface AttendanceMapper {
	// 개별 작성
	// 날짜, 직원번호, 이름, 상태, 출근시간, 퇴근시간
	public void insertAttendance(AttendanceVO attendance); // 수동 등록
	public void autoInsertAttendance(String empId); // 출근 버튼용
	//@Update("UPDATE attendance SET CHECK_OUT_TIME = now(), UPDATE_AT = NOW() WHERE EMP_ID = #{empId} AND WORK_DATE = now() AND CHECK_OUT_TIME IS NULL")
	public void autoUpdateCheckout(String empId);; // 퇴근 버튼용
	// 출근 개별 조회
	//public AttendanceVO selectAttendance(Long empId);
	public AttendanceVO selectAttendance(Long attendanceId);
	
	// empId에 따른 전체 근태 목록
	public List<AttendanceVO> selectAttendanceList(Long empId);
	// 전체 데이터 조회(1개)
	public MemberVO selectList(Map<String,Object> map);
	public Integer selectRowCount(Map<String,Object> map);
	// update(출,퇴근 수정)
	//public void updateAttendance(Long empId);
	public void updateAttendance(AttendanceVO attendance);
	
	// delete (출,퇴근 삭제)
	@Delete("DELETE FROM attendance WHERE attendance_id=#{attendanceId}")
	public void deleteAttendance(Long attendanceId);
	
	
}
