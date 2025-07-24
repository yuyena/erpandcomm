package kr.spring.vacation.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VacationBalanceVO {
	private int balanceId;
	private Long empId; // 사용자 번호
	private String userName; //사용자 이름
	private String vacationType; // 휴가 유형
	private int totalDays; // 휴가 총일수
	private int usedDays; // 사용한 휴가
	private int remainingDays;// 남은 휴가
	private String notes; //비고
	private String createdAt; // 생성날짜
	private String updatedAt;// 수정 날짜
	private Long departmentNum; // 부서 번호
	private String departmentName; // 부서 이름
	private int annual;  // 연차 기본 휴가일수
    private int half;    // 반차 기본 휴가일수
    private int special; // 특별
    private String positionName; // 직급이름
    private String positionCode; // 직급코드
}
