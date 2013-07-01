package org.springframework.integration.websocket.stomp.outbound;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.websocket.outbound.WebSocketMessageOutboundTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.integration.websocket.stomp.SubscriptionRegistry;
import org.springframework.web.messaging.converter.CompositeMessageConverter;
import org.springframework.web.messaging.converter.MessageConverter;
import org.springframework.web.messaging.stomp.StompCommand;
import org.springframework.web.messaging.stomp.support.StompHeaderAccessor;
import org.springframework.web.messaging.stomp.support.StompMessageConverter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

public final class StompWebSocketMessageOutboundTransformer implements WebSocketMessageOutboundTransformer {

	private final StompMessageConverter stompMessageConverter = new StompMessageConverter();

	private final SubscriptionRegistry subscriptionRegistry;

	private final Log logger = LogFactory.getLog(getClass());

	private MessageConverter payloadConverter = new CompositeMessageConverter(null);

	public StompWebSocketMessageOutboundTransformer(SubscriptionRegistry subscriptionRegistry) {
		this.subscriptionRegistry = subscriptionRegistry;
	}

	@Override
	public WebSocketMessage<?> transform(Message<?> message) {
		StompHeaderAccessor stompHeaders = StompHeaderAccessor.wrap(message);
		stompHeaders.setStompCommandIfNotSet(StompCommand.MESSAGE);

		if (stompHeaders.getStompCommand() == StompCommand.MESSAGE && stompHeaders.getSubscriptionId() == null) {
			Set<String> subscriptions = this.subscriptionRegistry.getSubscriptions(stompHeaders.getSessionId(), stompHeaders.getDestination());
			stompHeaders.setSubscriptionId(subscriptions.iterator().next());
			// TODO Cope with multiple subscriptions to the same destination
		}

		byte[] payload;
		try {
			// TODO Can this payload conversion based on the media type be moved into Spring and reused?
			MediaType contentType = stompHeaders.getContentType();
			payload = payloadConverter.convertToPayload(message.getPayload(), contentType);
		}
		catch (Exception e) {
			logger.error("Failed to send " + message, e);
			throw new MessagingException(message, e);
		}

		Message<byte[]> byteMessage = MessageBuilder.withPayload(payload).copyHeaders(stompHeaders.toMap()).build();
		byte[] bytes = stompMessageConverter.fromMessage(byteMessage);

		return new TextMessage(new String(bytes));
	}
}
