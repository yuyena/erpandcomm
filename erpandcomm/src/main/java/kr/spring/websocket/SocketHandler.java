package kr.spring.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SocketHandler extends TextWebSocketHandler {

	private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();

	/*
	 * 클라이언트가 연결되면, 클라이언트와 관련된 WebSocketSession을 users 맵에 저장한다. 이 users 맵은
	 * 채팅 메시지를 연결된 전체 클라이언트에 전달할 때 사용
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("웹소켓 연결 성공: {}", session.getId());
		log.debug("세션 URI: {}", session.getUri());
		users.put(session.getId(), session);
	}

	/*
	 * 클라이언트와의 연결이 종료되면, 클라이언트에 해당하는 WebSocketSession을 users 맵에서 제거한다.
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("웹소켓 연결 종료: {}, 상태: {}", session.getId(), status);
		users.remove(session.getId());
	}

	/*
	 * 클라이언트가 전송한 메시지를 users 맵에 보관한 전체 WebSocketSession에 다시 전달한다. 클라이언트는
	 * 메시지를 수신하면 채팅 영역에 보여주도록 구현, 특정 클라이언트가 채팅 메시지를 서버에 보내면 전체 클라이언트는
	 * 다시 그 메시지를 받아서 화면에 보여주게 된다.
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.debug("메시지 수신 from {}: {}", session.getId(), payload);
		log.debug("현재 연결된 세션 수: {}", users.size());
		
		for (WebSocketSession s : users.values()) {
			if (s.isOpen()) {
				try {
			s.sendMessage(message);
					log.debug("메시지 전송 to {}: {}", s.getId(), payload);
				} catch (IOException e) {
					log.error("메시지 전송 실패 to {}: {}", s.getId(), e.getMessage());
				}
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("웹소켓 전송 에러 - 세션: {}", session.getId());
		log.error("에러 내용: {}", exception.getMessage());
		
		if (session.isOpen()) {
			session.close(CloseStatus.SERVER_ERROR);
		}
		users.remove(session.getId());
	}
}
