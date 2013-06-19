package org.springframework.integration.websocket.outbound;

import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

public class DefaultWebSocketMessageOutboundTransformer implements WebSocketMessageOutboundTransformer {

	@Override
	public WebSocketMessage<?> transform(Message<?> message) {
		Assert.isInstanceOf(String.class, message.getPayload());
		return new TextMessage((String) message.getPayload());
	}

}
