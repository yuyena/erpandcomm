package kr.spring.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GradeVO {
    private Long grade_num; // 등급번호
    private String grade_name; // 등급명
    private String grade_code; // 등급코드
}
