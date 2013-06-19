package org.springframework.integration.websocket.inbound;

import org.springframework.messaging.Message;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public interface WebSocketMessageInboundTransformer {

	Message<?> transform(WebSocketMessage<?> webSocketMessage, WebSocketSession webSocketSession);

}
