package kr.spring.chat.vo;

import kr.spring.util.DurationFromNow;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChatMessageReadVO {
	
	private long message_num; // 읽은 메시지
	private long user_num; // 읽은 사람 번호
	private String read_at; // 읽은 시간
	
	public void setRead_at(String read_at) {
		this.read_at = DurationFromNow.getTimeDiffLabel(read_at);
	}

}
