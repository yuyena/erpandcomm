package kr.spring.vacation.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.spring.vacation.vo.VacationBalanceVO;
import kr.spring.vacation.vo.VacationVO;

@Mapper
public interface VacationMapper {
	// 휴가 신청
	public void insertVacation(VacationVO vacation);
	// 휴가 내역 조회
	public List<VacationVO> selectVacationList(Long empId);
	// 휴가 내역 검색 조회
	public List<VacationVO> selectList(Map<String, Object> map);
	// 휴가 상세 내역
	public VacationVO selectVacationDetail(Long requestId);
	// 휴가 사용내역 조회
	public VacationBalanceVO selectVacationSummary(@Param("empId")Long empId);
	//총 휴가 일수
	//public Integer selectTotalVacationDays(Long empId);
	// 사용한 휴가 일수 조회
	//public Integer selectUsedVacationDays(Long empId);
	// 남은 휴가 일수 조회
	//public Integer selectRemainingVacationDays(Long empId);
	// 삭제
	@Delete("DELETE FROM vacation_request WHERE request_id = #{requestId}")
	public void deleteVacation(Long requestId);
}
