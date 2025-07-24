package kr.spring.vacation.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PositionVO {
	private long positionNum; // 직급번호
	private String positionName; // 직급이름
	private String positionCode; // 직급 코드
	private int baseAnnual; //기본 연차
	private int baseHalf; //기본 반차
	private int baseSpecial; //특별 휴가
	
}
