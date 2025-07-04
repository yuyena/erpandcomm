package kr.spring.chat.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import oracle.sql.DATE;

@Getter
@Setter
@ToString
public class ChatMemberVO {
	
	private long member_num; // 멤버번호 pk
	private long room_num; // 채팅방 번호
	private long user_num; // 사용자 번호
	private String role;; // 방에서 역할(방장/멤버)
	private DATE joined_at; // 채팅방 입장 시간
	private String is_active; // 활성화 여부(Y/N)

}
