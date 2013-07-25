package org.springframework.integration.websocket.stomp.outbound;

import org.springframework.integration.websocket.outbound.WebSocketMessageOutboundTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.handler.MutableUserQueueSuffixResolver;
import org.springframework.messaging.simp.stomp.ToClientHandler;
import org.springframework.web.socket.WebSocketMessage;

public final class StompWebSocketMessageOutboundTransformer implements WebSocketMessageOutboundTransformer {

	private final ToClientHandler toClientHandler = new ToClientHandler();

	@Override
	public WebSocketMessage<?> transform(Message<?> message) {
		return toClientHandler.handle(message);
	}
}
