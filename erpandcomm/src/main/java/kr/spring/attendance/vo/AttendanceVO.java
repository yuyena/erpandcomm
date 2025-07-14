package kr.spring.attendance.vo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
	private Date workDate; // 일자
	private Date scheduledInTime; // 출근 예정
	private Date scheduledOutTime; // 퇴근 예정
	private Long empId; // empUser
	private String employeeCode; //사원번호
	@NotBlank
	private String empName; // 사원 이름
	@NotBlank
	private String workType; // 근무 형태 (
	@NotNull
	private Date checkIntime; // 출근시간
	@NotNull
	private Date checkOuttime; // 퇴근시간
	private String notes; // 비고
	private Date createdAt;
	private Date updatedAt;
	private String status; // 출근 형태(출근, 결근, 지각, 휴가,조퇴) 
	
	// 숫자 형태의 workType 값을 문자열로 변환
	public String getworkTypeName() {
		String name;
		switch(workType) {
		case "1" : name = "normal"; break;
		case "2" : name = "overtime"; break;
		case "3" : name = "night"; break;
		case "4" : name = "holiday"; break;
		default : name = "분류오류";
		}
		return name;
	}
	
	
}
