package kr.spring.member.vo;

import java.io.IOException;
import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"photo"})
public class MemberVO {
	
	private long user_num;
	@Pattern(regexp="^[A-Za-z0-9]{10,11}$")
	private String employee_code;
	private String authority;
	@NotBlank
	private String user_name;
	private String resident_reg_num;
	@Pattern(regexp="^[A-Za-z0-9]{4,12}$")
	private String passwd;
	@NotBlank
	private String phone;
	@Email
	@NotBlank
	private String email;
	private String extension_num;
	private byte[] photo;
	private String photo_name;
	private Date hire_date;
	private Date resignation_date;
	private long salary;
	private long department_num;
	private long position_num;
	private long grade_num;
	
	private String department_name;
	
	
	// 비밀번호 변경 시 현재 비밀번호를 저장하는 용도로 사용
	@Pattern(regexp="^[A-Za-z0-9]{4,12}$")
	private String now_passwd;
	
	
	// 비밀번호 일치 여부 체크
	public boolean isCheckedPassword(String userPasswd) {
		if (getAuthorityOrdinal() > 1 && passwd.equals(userPasswd)) {
			return true;
		} // if
		return false;
	}
	
	public int getAuthorityOrdinal() {
		
		if (authority == null) return -1;
		
		if (authority.equals(UserRole.INACTIVE.getValue())) {
			return UserRole.INACTIVE.ordinal(); // 0
		} else if (authority.equals(UserRole.SUSPENDED.getValue())) {
			return UserRole.SUSPENDED.ordinal(); // 1
		} else if (authority.equals(UserRole.USER.getValue())) {
			return UserRole.USER.ordinal(); // 2
		} else if (authority.equals(UserRole.ADMIN.getValue())) {
			return UserRole.ADMIN.ordinal(); // 3
		} else {
			return -1;
		} // if
		
	}
	
	// 이미지 BLOB 처리
	public void setUpload(MultipartFile upload) throws IOException {
		
		
		// MultipartFile -> byte[]
		setPhoto(upload.getBytes());
		// 파일 이름
		setPhoto_name(upload.getOriginalFilename());
	}

}










































