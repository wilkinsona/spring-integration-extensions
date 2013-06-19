package org.springframework.integration.websocket.stomp;

import java.util.Set;

import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.messaging.stomp.StompCommand;
import org.springframework.web.messaging.stomp.support.StompHeaderAccessor;

public final class StompConnectHandlingChannelInterceptor extends ChannelInterceptorAdapter {

	private final MessageChannel outputChannel;

	public StompConnectHandlingChannelInterceptor(MessageChannel outputChannel) {
		this.outputChannel = outputChannel;
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		if (headerAccessor.getStompCommand() == StompCommand.CONNECT) {

			StompHeaderAccessor connectedHeaderAccessor = StompHeaderAccessor.create(StompCommand.CONNECTED);
			Set<String> acceptVersions = headerAccessor.getAcceptVersion();
			if (acceptVersions.contains("1.2")) {
				connectedHeaderAccessor.setVersion("1.2");
			}
			else if (acceptVersions.contains("1.1")) {
				connectedHeaderAccessor.setVersion("1.1");
			}
			else if (acceptVersions.isEmpty()) {
				// 1.0
			}
			connectedHeaderAccessor.setHeartbeat(0, 0);

			connectedHeaderAccessor.setSessionId(headerAccessor.getSessionId());

			Message<String> connectedMessage = MessageBuilder.withPayload("")
				.copyHeaders(connectedHeaderAccessor.toMap())
				.build();

			this.outputChannel.send(connectedMessage);
		}
		return message;
	}
}
