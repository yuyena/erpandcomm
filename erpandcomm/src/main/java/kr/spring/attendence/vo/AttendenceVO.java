package kr.spring.attendence.vo;

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
public class AttendenceVO {
	/*
	 * 일자, 사원번호, 사원명, 근무형태
	 * 출근시간, 퇴근시간
	 * 근무형태: normal/overtime/night/holiday
	 */
	private Long attendanceId; //출퇴근 id
	private Date workDate; // 일자
	private Long empId; // 사원번호
	@NotBlank
	private String empName; // 사원 이름
	@NotBlank
	private String workType; // 근무 형태
	@NotNull
	private Date checkIntime; // 출근시간
	@NotNull
	private Date checkOuttime; // 퇴근시간
	private Date createdAt;
	private Date updatedAt;
	
	
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
