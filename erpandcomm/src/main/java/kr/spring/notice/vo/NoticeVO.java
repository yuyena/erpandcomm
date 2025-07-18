package kr.spring.notice.vo;

import java.sql.Date;

import jakarta.validation.constraints.NotBlank;
import kr.spring.member.vo.MemberVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeVO {
	
	private long noti_num;
	private long user_num;
	@NotBlank
	private String noti_title;
	@NotBlank
	private String noti_content;
	private Date noti_date;
	
	private MemberVO memberVO;

}
