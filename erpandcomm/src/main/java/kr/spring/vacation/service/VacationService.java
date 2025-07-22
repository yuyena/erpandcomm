package kr.spring.vacation.service;

import java.util.List;
import java.util.Map;

import kr.spring.vacation.vo.VacationVO;


public interface VacationService {
	public void insertVacation(VacationVO vacation);
	public List<VacationVO> selectVacationList(Long empId);
	public List<VacationVO> selectList(Map<String, Object> map);
	public VacationVO selectVacationDetail(Long requestId);
	public Integer selectTotalVacationDays(Long empId);
	public Integer selecUsedVacationDays(Long empId);
	public void updateVacation(VacationVO vacation);
	public void deleteVacation(Long requestId);
}
