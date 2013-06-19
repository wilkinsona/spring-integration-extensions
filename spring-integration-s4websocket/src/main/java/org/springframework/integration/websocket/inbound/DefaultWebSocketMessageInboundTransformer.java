package org.springframework.integration.websocket.inbound;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.websocket.support.WebSocketHeaders;
import org.springframework.messaging.Message;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class DefaultWebSocketMessageInboundTransformer implements WebSocketMessageInboundTransformer {

	@Override
	public Message<?> transform(WebSocketMessage<?> wsMessage, WebSocketSession wsSession) {
		return MessageBuilder.withPayload(wsMessage.getPayload())
				.setHeader(WebSocketHeaders.WS_SESSION_ID, wsSession.getId())
				.build();
	}

}
