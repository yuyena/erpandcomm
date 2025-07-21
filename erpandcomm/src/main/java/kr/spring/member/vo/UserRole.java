package kr.spring.member.vo;

import lombok.Getter;

@Getter
public enum UserRole {
	
	// 탈퇴, 정지, 회원, 관리자
	INACTIVE("ROLE_INACTIVE"), SUSPENDED("ROLE_SUSPENDED"), USER("USER"), ADMIN("ROLE_ADMIN"), MANAGER("MANAGER");
	
	private String value;

	UserRole(String value) {
		this.value = value;
	}

}
