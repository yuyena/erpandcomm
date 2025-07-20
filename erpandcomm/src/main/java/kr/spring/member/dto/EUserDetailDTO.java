package kr.spring.member.dto;

import lombok.Data;

@Data
public class EUserDetailDTO {
    private Long userNum; // 사번(유저번호)
    private String userName; // 이름
    private String passwd;   // 비밀번호
    private String phone;    // 전화번호
    private String email;    // 이메일
    private String photo;    // 사진 파일명
    private String hireDate; // 입사일
    private String resignationDate; // 퇴사일
    private Integer salary;  // 연봉
    private String departmentNum; // 부서번호
    private String positionNum;   // 직급번호
    private String gradeNum;      // 등급번호
    private String extensionNum;  // 내선번호
    private String residentRegNum; // 주민등록번호
}
