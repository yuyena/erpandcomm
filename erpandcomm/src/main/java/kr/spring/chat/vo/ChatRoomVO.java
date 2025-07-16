package kr.spring.chat.vo;

import kr.spring.member.vo.MemberVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import oracle.sql.DATE;

@Getter
@Setter
@ToString
public class ChatRoomVO {
	
	private long room_num; // 채팅방 고유 번호
	private String room_name; // 채팅방 이름
	private String room_type; // 채팅방 타입(1:1, group 등)
	private String description; // 채팅방 설명
	private long created_by; // 채팅방 생성자(유저번호)
	private Integer max_members; // 최대 인원(옵션)
	private String is_active;
	private DATE created_at;
	private DATE updated_at;
	
	private MemberVO memberVO;
	private boolean isOwner; // 현재 로그인한 사용자가 방장인지 여부
	private String role; // 채팅멤버의 역할(방장/멤버)
	
	// 안읽은 메시지 존재 여부 (채팅방 목록에서 빨간 점 표시용)
	private boolean hasUnreadMessages;
}
