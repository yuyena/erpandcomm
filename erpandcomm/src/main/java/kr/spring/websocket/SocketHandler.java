package kr.spring.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketHandler extends TextWebSocketHandler {

    // roomId별로 세션을 관리
    private Map<String, Map<String, WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = extractRoomId(session);
        String sessionId = session.getId();
        
        log.debug("새로운 웹소켓 연결 - sessionId: {}, roomId: {}", sessionId, roomId);
        
        // 해당 방의 세션 맵이 없으면 생성
        roomSessions.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>());
        
        // 세션을 방에 추가
        roomSessions.get(roomId).put(sessionId, session);
        
        log.debug("방 {} 의 현재 접속자 수: {}", roomId, roomSessions.get(roomId).size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomId(session);
        String sessionId = session.getId();
        
        log.debug("웹소켓 연결 종료 - sessionId: {}, roomId: {}", sessionId, roomId);
        
        // 방에서 세션 제거
        if (roomSessions.containsKey(roomId)) {
            roomSessions.get(roomId).remove(sessionId);
            
            // 방에 아무도 없으면 방 제거
            if (roomSessions.get(roomId).isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = extractRoomId(session);
        
        log.debug("메시지 수신 - roomId: {}, message: {}", roomId, message.getPayload());
        
        // 해당 방의 모든 세션에 메시지 전송
        Map<String, WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.values().forEach(s -> {
                try {
                    s.sendMessage(message);
                } catch (Exception e) {
                    log.error("메시지 전송 실패", e);
                }
            });
        }
    }

    private String extractRoomId(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("웹소켓 전송 오류 - sessionId: " + session.getId(), exception);
    }
}
