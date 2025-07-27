package kr.spring.vacation.service;

import java.util.List;
import java.util.Map;

import kr.spring.vacation.vo.VacationBalanceVO;
import kr.spring.vacation.vo.VacationVO;

public interface VacationService {
	// 휴가 목록 확인
	public void insertVacation(VacationVO vacation);
	public List<VacationVO> selectVacationList(Long empId);
	public List<VacationVO> selectList(Map<String, Object> map);
	public VacationVO selectVacationDetail(Long requestId);
	// 휴가 일수 
	public VacationBalanceVO selectVacationSummary(Long empId);
	//public Integer selectTotalVacationDays(Long empId);
	//public Integer selectUsedVacationDays(Long empId);
	//public Integer selectRemainingVacationDays(Long empId);
	// 휴가 삭제
	public void deleteVacation(Long requestId);
}
