package kr.spring.vacation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.vacation.dao.VacationMapper;
import kr.spring.vacation.vo.VacationVO;

@Service
@Transactional
public class VacationServicImpl implements VacationService{
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
	public VacationVO selectVacationDetail(Long vacationId) {
		return null;
	}

	@Override
	public Integer selectTotalVacationDays(Long empId) {
		return null;
	}

	@Override
	public Integer selecUsedVacationDays(Long empId) {
		return null;
	}

	@Override
	public void updateVacation(VacationVO vacation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelVacation(Long vacationId) {
		// TODO Auto-generated method stub
		
	}
}
