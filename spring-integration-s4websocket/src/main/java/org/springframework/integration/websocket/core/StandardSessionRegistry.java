package org.springframework.integration.websocket.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

public class StandardSessionRegistry implements SessionRegistry {

	private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<String, WebSocketSession>();

	@Override
	public void putSession(WebSocketSession session) {
		sessions.put(session.getId(),  session);
	}

	@Override
	public WebSocketSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}

	@Override
	public void removeSession(String sessionId) {
		sessions.remove(sessionId);
	}

}
