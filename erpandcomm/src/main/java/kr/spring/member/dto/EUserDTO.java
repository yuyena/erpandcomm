package kr.spring.member.dto;

import lombok.Data;

@Data
public class EUserDTO {
    private Long userNum; // 사번(유저번호)
    private String employeeCode; // 사원코드
    private String authority;    // 권한
}
