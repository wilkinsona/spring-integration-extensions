package org.springframework.integration.websocket.outbound;

import org.springframework.messaging.Message;
import org.springframework.web.socket.WebSocketMessage;

public interface WebSocketMessageOutboundTransformer {

	WebSocketMessage<?> transform(Message<?> message);

}
