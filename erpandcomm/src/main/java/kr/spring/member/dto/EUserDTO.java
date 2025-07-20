package kr.spring.member.dto;

import lombok.Data;

@Data
public class EUserDTO {
    private String employeeCode; // 사원코드
    private String authority;    // 권한
}
