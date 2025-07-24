package kr.spring.payroll.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.spring.payroll.dao.PayrollMapper;
import kr.spring.payroll.vo.PayrollVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PayrollServiceImpl implements PayrollService {
	
	@Autowired
	private PayrollMapper payrollMapper;
	
	@Override
	public void insertPayroll(PayrollVO payroll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PayrollVO> selectPayroll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PayrollVO> seletPayrollByEmpId(Long empId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePayroll(PayrollVO payroll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePayroll(Long payrollId) {
		// TODO Auto-generated method stub
		
	}

}
