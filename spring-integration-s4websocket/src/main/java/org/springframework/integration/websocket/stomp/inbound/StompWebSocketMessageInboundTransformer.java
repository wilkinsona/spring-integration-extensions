package org.springframework.integration.websocket.stomp.inbound;

import org.springframework.integration.websocket.inbound.WebSocketMessageInboundTransformer;
import org.springframework.messaging.Message;
import org.springframework.web.messaging.stomp.support.StompMessageConverter;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


public final class StompWebSocketMessageInboundTransformer implements WebSocketMessageInboundTransformer {

	private final StompMessageConverter stompMessageConverter = new StompMessageConverter();

	@Override
	public Message<?> transform(WebSocketMessage<?> webSocketMessage, WebSocketSession webSocketSession) {
		return stompMessageConverter.toMessage(webSocketMessage.getPayload(), webSocketSession.getId());
	}

}
