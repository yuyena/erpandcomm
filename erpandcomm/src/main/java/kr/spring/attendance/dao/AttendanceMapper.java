package kr.spring.attendance.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import kr.spring.attendance.vo.AttendanceVO;

@Mapper
public interface AttendanceMapper {
	// 개별 작성
	// 날짜, 직원번호, 이름, 상태, 출근시간, 퇴근시간
	@Insert("INSERT INTO attendance(WORK_DATE, EMP_ID, USER_NAME, STATUS,CHECK_IN_TIME,CHECK_OUT_TIME, NOTES,CREATED_AT,UPDATED_AT)"
			+ "VALUES (#{workDate},#{empId}, #{empName}, #{status}, #{checkIntime},#{checkOuttime}, #{notes}, NOW(), NOW())")
	public void insertAttendance(AttendanceVO attendence); // 수동 등록
	@Insert("INSERT INTO attendance(WORK_DATE,EMP_ID,STATUS,CHECK_IN_TIME, CHECK_OUT_TIME) VALUES(#{workDate},#{empId}, #{empName}, 'present', #{checkIntime},#{checkOuttime})")
	public void autoInsertAttendance(String empId); // 출근 버튼용
	public void autoUpdateCheckout(String empId);; // 출퇴근 버튼용
	// 출근 개별 조회
	public AttendanceVO selectAttendance(Long empId);
	// 전체 데이터 조회
	public List<AttendanceVO> selectList(Map<String,Object> map);
	public Integer selectRowCount(Map<String,Object> map);
	// update(출,퇴근 수정)
	public void updateAttendance(AttendanceVO attendence);
	// delete (출,퇴근 삭제) 
	public void deleteAttendance(Long empId);
	
	
}
