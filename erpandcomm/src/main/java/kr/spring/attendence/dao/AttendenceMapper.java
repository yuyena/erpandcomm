package kr.spring.attendence.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.attendence.vo.AttendenceVO;

@Mapper
public interface AttendenceMapper {
	// 개별 작성
	public void insertAttendence(AttendenceVO attendence);
	// 출근 개별 조회
	public AttendenceVO selectAttendence(Long empId);
	// 전체 데이터 조회
	public List<AttendenceVO> selectList(Map<String,Object> map);
	public Integer selectRowCount(Map<String,Object> map);
	// update(출,퇴근 수정)
	public void updateAttendence(AttendenceVO attendence);
	// delete (출,퇴근 삭제) 
	public void deleteAttendence(Long empId);
	
	
}
