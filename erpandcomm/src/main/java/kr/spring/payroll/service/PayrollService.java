package kr.spring.payroll.service;

import java.util.List;

import kr.spring.payroll.vo.PayrollVO;

public interface PayrollService {
	public void insertPayroll (PayrollVO payroll);
	public List<PayrollVO> selectPayroll();
	public List<PayrollVO> seletPayrollByEmpId(Long empId);
	public void updatePayroll(PayrollVO payroll);
	public void deletePayroll(Long payrollId);
}
