package org.springframework.integration.websocket.stomp.inbound;

import java.util.Set;

import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.websocket.stomp.SubscriptionRegistry;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.messaging.stomp.StompCommand;
import org.springframework.web.messaging.stomp.StompConversionException;
import org.springframework.web.messaging.stomp.support.StompHeaderAccessor;

public final class InboundStompChannelInterceptor extends ChannelInterceptorAdapter {

	private static final byte[] EMPTY_PAYLOAD = new byte[0];

	private final MessageChannel outputChannel;

	private final SubscriptionRegistry subscriptionRegistry;

	public InboundStompChannelInterceptor(MessageChannel outputChannel, SubscriptionRegistry subscriptionRegistry) {
		this.outputChannel = outputChannel;
		this.subscriptionRegistry = subscriptionRegistry;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
		if (headers.getStompCommand() == StompCommand.CONNECT) {
			handleConnect(headers);
		} else if (headers.getStompCommand() == StompCommand.SUBSCRIBE) {
			handleSubscribe(headers);
		}
		return message;
	}

	private void handleSubscribe(StompHeaderAccessor headers) {
		String sessionId = headers.getSessionId();
		String destination = headers.getDestination();
		String subscriptionId = headers.getSubscriptionId();

		this.subscriptionRegistry.registerSubscription(sessionId, subscriptionId, destination);
	}

	private void handleConnect(StompHeaderAccessor connectHeaders) {
		StompHeaderAccessor connectedHeaders = StompHeaderAccessor.create(StompCommand.CONNECTED);

		setAcceptVersion(connectHeaders.getAcceptVersion(), connectedHeaders);
		connectedHeaders.setSessionId(connectHeaders.getSessionId());
		connectedHeaders.setHeartbeat(0,0); // TODO

		// TODO: security

		Message<?> connectedMessage = MessageBuilder.withPayload(EMPTY_PAYLOAD).copyHeaders(connectedHeaders.toMap()).build();

		this.outputChannel.send(connectedMessage);
	}

	private void setAcceptVersion(Set<String> acceptVersions, StompHeaderAccessor connectedHeaders) {
		if (acceptVersions.contains("1.2")) {
			connectedHeaders.setAcceptVersion("1.2");
		}
		else if (acceptVersions.contains("1.1")) {
			connectedHeaders.setAcceptVersion("1.1");
		}
		else if (acceptVersions.isEmpty()) {
			// 1.0
		}
		else {
			throw new StompConversionException("Unsupported version '" + acceptVersions + "'");
		}
	}
}
