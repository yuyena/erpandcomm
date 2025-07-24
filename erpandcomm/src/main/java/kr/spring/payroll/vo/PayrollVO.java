package kr.spring.payroll.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PayrollVO {
	private Long payrollId; // 급여 고유  ID
	private Long empId; // 직원번호(ID)
	private String salaryMonth; // 직원 급여 날
	private String salaryDay; // 직원 급여 날
	private String empName; // 직원 이름
	private int baseSalary; // 기본급
	private int bonus; // 상여급(보너스)
	private int dedeductions; //공제액 (총 공제)
	private int totalPay; // 총 지급액(기본급 + 상여)
	private int actualPay ; // 실지급액(총 지급 - 공제)
	private String status; //급여 상태(정상, 지급완료, 보류 등)
	private int tax; // 세금
	private int insurance; // 보험
	private int etxDeduction; //기타 공제
	private Date created_at; //
	private Date updated_at; //
	private String notes; // 비고
	
	public enum PayrollStatus{
		PENDING, //미처리
		PAID,    // 지급
		HOLD,   // 지급 보류
		CANCELLED // 취소
	}
}
