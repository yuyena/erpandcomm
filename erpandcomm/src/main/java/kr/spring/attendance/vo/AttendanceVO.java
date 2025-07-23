package kr.spring.attendance.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AttendanceVO {
	/*
	 * 일자, 사원번호, 사원명, 근무형태
	 * 출근시간, 퇴근시간
	 * 근무형태: normal/overtime/night/holiday
	 * 출근유형 : 출근, 결근, 지각, 휴가,조퇴
	 */
	private Long attendanceId; //출퇴근 id
	@NotBlank
	private String workDate; // 일자
	private String scheduledInTime; // 출근 예정
	private String scheduledOutTime; // 퇴근 예정
	private Long empId; // 실제 DB에서는 attendance.emp_id = euser_detail.user_num
	private String employeeCode; //사원번호 조회용 - employee_code from euser
	private String empName; // 사원이름 조회용 - user_name from euser_detail
	@NotBlank
	private String workType; // 근무 형태 (
	@NotBlank
	private String checkIntime; // 출근시간
	private String checkOuttime; // 퇴근시간
	private String notes; // 비고
	private Date createdAt;
	private Date updatedAt;
	@NotBlank
	private String status; // 출근 형태(출근, 결근, 지각, 휴가,조퇴) 
	
	public String getStatusName() {
		switch (status) {
		case "present": return "출근";
		case "late": return "지각";
		case "early_leave": return "조퇴";
		case "absent": return "결근";
		case "holiday": return "휴가";
		default : return "알 수 없음";
		}
	}
	
	public String getWorkTypeName() {
		switch (workType) {
		case "normal": return "정상 근무";
		case "overtime": return "초과 근무";
		case "night": return "야간 근무";
		case "holiday": return "휴일 근무";
		default : return "알 수 없음";
		
		}
	}

	
}
