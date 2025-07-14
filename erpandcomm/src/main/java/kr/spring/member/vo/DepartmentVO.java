package kr.spring.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DepartmentVO {
    private Long department_num; // 부서번호
    private String department_code; // 부서코드
    private String department_name; // 부서명
    private Long manager_num; // 부서장 번호
}
