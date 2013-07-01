package org.springframework.integration.websocket.core;

import org.springframework.web.socket.WebSocketSession;

/**
 * A WebSocketSessionListener is notified of WebSocketSessions beginning and ending
 *
 * @author Andy Wilkinson
 */
public interface WebSocketSessionListener {

	/**
	 * Notification that the web socket session began
	 *
	 * @param webSocketSession the session that has begun
	 */
	void sessionBegan(WebSocketSession webSocketSession);

	/**
	 * Notification that the web socket session has ended
	 *
	 * @param webSocketSession the session that has ended
	 */
	void sessionEnded(WebSocketSession webSocketSession);
}
