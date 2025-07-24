package kr.spring.payroll.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.spring.payroll.vo.PayrollVO;

@Mapper
public interface PayrollMapper {
	// 담당자
	// 입력
	public void insertPayroll (PayrollVO payroll);
	// 조회
	public List<PayrollVO> selectPayroll();
	// 한 직원의 급여 전체 조회
	public List<PayrollVO> seletPayrollByEmpId(Long empId);
	// 수정
	public void updatePayroll(PayrollVO payroll);
	// 삭제
	public void deletePayroll(Long payrollId);
}
