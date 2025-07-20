package kr.spring.vacation.vo;

public class VacationVO {
	private Long requestId; //신청 ID
	private Long empId; // 직원 ID
	private String vacationType; //휴가 구분 (annual/sick/personal/maternity/special)
	private String start_date; // 시작일
	private String end_date; // 종료일
	private int total_days; // 총 일수
	private String reason; // 사유
	private String status; // 승인 상태 (pending/approved/rejected/cancelled)
	private String request_date; // 신청일
	private String created_at; // 생성일
	private String updated_at; // 수정일
	
	
	
}
