package org.springframework.integration.websocket.stomp.inbound;

import org.springframework.integration.websocket.inbound.WebSocketMessageInboundTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompMessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


public final class StompWebSocketMessageInboundTransformer implements WebSocketMessageInboundTransformer {

	private final StompMessageConverter stompMessageConverter = new StompMessageConverter();

	@Override
	public Message<?> transform(WebSocketMessage<?> webSocketMessage, WebSocketSession session) {
		Message<?> message = this.stompMessageConverter.toMessage(webSocketMessage.getPayload());

		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
		headers.setSessionId(session.getId());
		headers.setUser(session.getPrincipal());

		return MessageBuilder.withPayloadAndHeaders(message.getPayload(), headers).build();
	}

}
