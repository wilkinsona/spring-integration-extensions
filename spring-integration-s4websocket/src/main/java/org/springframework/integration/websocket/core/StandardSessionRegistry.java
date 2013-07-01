package org.springframework.integration.websocket.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

public class StandardSessionRegistry implements SessionRegistry, WebSocketSessionListener {

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>();

	@Override
	public WebSocketSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}

	@Override
	public void sessionBegan(WebSocketSession webSocketSession) {
		sessions.put(webSocketSession.getId(),  webSocketSession);

	}

	@Override
	public void sessionEnded(WebSocketSession webSocketSession) {
		sessions.remove(webSocketSession.getId());
	}

}
