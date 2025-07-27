package kr.spring.vacation.vo;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class VacationVO {
	private Long requestId; //신청 ID
	private Long empId; // 직원 ID 
	private String vacationType; //휴가 구분 (annual/sick/personal/maternity/special)
	private String startDate; // 시작일
	private String endDate; // 종료일
	private String startTime; // 시작시간
	private String endTime; // 종료시간
	private int totalDays; // 총 일수
	private String reason; // 사유
	private String status; // 승인 상태 (pending/approved/rejected/cancelled)
	private String requestDate; // 신청일
	private String createdAt; // 생성일
	private String updatedAt; // 수정일

	public void setTime() {
		if (this.startTime == null || this.endTime == null) {
			switch (vacationType) {
			case "연차":
			case "반차":
			case "병가":
			case "개인사유":
			case "특별휴가":
				this.startTime = "09:00";
				this.endTime = "18:00";
				break;
				/*
			case "출산휴가":
				this.startTime = "00:00";
				this.endTime = "23:59";
				break;
			*/
			}
		}
	}
	public String getVacationTypeName() {
		switch (vacationType) {
		case "annual" : return "연차";
		case "half" : return "반차";
		case "sick" : return "병가";
		case "personal" : return "개인사유";
		// case "maternity" : return "출산휴가";
		case "special" : return "특별휴가";
		default: return "알 수 없음";
		}
	}
}
