package kr.spring.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PositionVO {
    private Long position_num; // 직급번호
    private String position_name; // 직급명
    private String position_code; // 직급코드
}
