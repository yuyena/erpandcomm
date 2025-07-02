package kr.spring.client.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClientVO {
    private long client_num;         // 거래처 식별 번호
    private String client_name;      // 거래처 이름
    private long client_type;        // 거래처 구분(0:공급, 1:고객)
    private String contact_person;   // 담당자
    private String phone;            // 연락처
    private String email;            // 이메일
    private String address1;         // 주소
    private String address2;         // 상세 주소
} 