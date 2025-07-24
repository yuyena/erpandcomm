package kr.spring.attendance.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchVO {
	private Long empId;
	private String startDate;
	private String endDate;
	private String empName; //조회 직원명
	private String status; // 근무 유형
}
