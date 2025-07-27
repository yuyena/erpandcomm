package kr.spring.vacation.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.vacation.dao.VacationMapper;
import kr.spring.vacation.vo.VacationBalanceVO;
import kr.spring.vacation.vo.VacationVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class VacationServiceImpl implements VacationService{
	@Autowired
	private VacationMapper vacationMapper;

	@Override
	public void insertVacation(VacationVO vacation) {
		vacation.setTime();
		vacationMapper.insertVacation(vacation);
		
	}

	@Override
	public List<VacationVO> selectVacationList(Long empId) {
		return vacationMapper.selectVacationList(empId);
	}

	@Override
	public VacationVO selectVacationDetail(Long requestId) {
		return vacationMapper.selectVacationDetail(requestId);
	}
	
	@Override
	public void deleteVacation(Long requestId) {
		vacationMapper.deleteVacation(requestId);
	}

	@Override
	public List<VacationVO> selectList(Map<String, Object> map) {
		return vacationMapper.selectList(map);
	}

	@Override
	public VacationBalanceVO selectVacationSummary(Long empId) {
		return vacationMapper.selectVacationSummary(empId);
	}
	
	

	
}
