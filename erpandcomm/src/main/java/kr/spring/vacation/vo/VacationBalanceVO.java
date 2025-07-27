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
	private int annual;  // 연차 기본 휴가일수
    private int half;    // 반차 기본 휴가일수
    private int special; // 특별
	private int totalDays; // 휴가 총일수
	private int usedDays; // 사용한 휴가
	private int remainingAnnual;// 남은 연차
	private int remainingHalf;// 남은 반차
	private int remainingSpecial;// 남은 특휴
	private int usedAnnual; // 사용한 연차
    private int usedHalf; //사용한 반차
    private int usedSpecial; //사용한 특휴
	private String notes; //비고
	private String createdAt; // 생성날짜
	private String updatedAt;// 수정 날짜
	private Long departmentNum; // 부서 번호
	private String departmentName; // 부서 이름
	private String positionName; // 직급이름
    private String positionCode; // 직급코드
}
