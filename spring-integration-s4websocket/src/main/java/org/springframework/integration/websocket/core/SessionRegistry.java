package org.springframework.integration.websocket.core;

import org.springframework.web.socket.WebSocketSession;

public interface SessionRegistry {

	WebSocketSession getSession(String sessionId);
}
