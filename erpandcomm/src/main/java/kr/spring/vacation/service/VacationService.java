package kr.spring.vacation.service;

import java.util.List;

import kr.spring.vacation.vo.VacationVO;

public interface VacationService {
	public void insertVacation(VacationVO vacation);
	public List<VacationVO> selectVacationList(Long empId);
	public VacationVO selectVacationDetail(Long vacationId);
	public Integer selectTotalVacationDays(Long empId);
	public Integer selecUsedVacationDays(Long empId);
	public void updateVacation(VacationVO vacation);
	public void cancelVacation(Long vacationId);
}
