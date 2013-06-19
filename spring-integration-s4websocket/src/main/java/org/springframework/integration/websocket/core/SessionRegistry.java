package org.springframework.integration.websocket.core;

import org.springframework.web.socket.WebSocketSession;

public interface SessionRegistry {

	void putSession(WebSocketSession session);

	WebSocketSession getSession(String sessionId);

	void removeSession(String sessionId);
}
