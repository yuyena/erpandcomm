package kr.spring.chat.vo;

import kr.spring.util.DurationFromNow;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessageVO {
	
	private long message_num; // 메시지 고유 번호
	private long room_num; // 채팅방 번호
	private long sender_num; // 보낸 사람(유저번호)
	private String content; // 메시지 내용
	private String sent_at; // 보낸 시간
	private String sent_date; // 보낸 날짜 (YYYY-MM-DD 형식)
	private String sender_name; // 보낸 사람 이름
	private int unread_count; // 안 읽은 사람 수
	
	public void setSent_at(String sent_at) {
		this.sent_at = DurationFromNow.getTimeDiffLabel(sent_at);
	}
}
