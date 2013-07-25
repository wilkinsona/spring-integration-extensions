package org.springframework.integration.websocket.stomp.inbound;

import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.handler.MutableUserQueueSuffixResolver;
import org.springframework.messaging.simp.stomp.ConnectHandler;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public final class InboundStompChannelInterceptor extends ChannelInterceptorAdapter {

	private final ConnectHandler connectHandler = new ConnectHandler();

	private final MessageChannel outputChannel;

	public InboundStompChannelInterceptor(MessageChannel outputChannel) {
		this.outputChannel = outputChannel;
	}

	public void setUserQueueSuffixResolver(MutableUserQueueSuffixResolver resolver) {
		connectHandler.setUserQueueSuffixResolver(resolver);
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor headers = StompHeaderAccessor.wrap(message);
		if (headers.getCommand() == StompCommand.CONNECT) {
			handleConnect(headers);
		}
		return message;
	}

	private void handleConnect(StompHeaderAccessor connectHeaders) {
		Message<?> connected = this.connectHandler.handleConnect(connectHeaders);
		this.outputChannel.send(connected);
	}
}
