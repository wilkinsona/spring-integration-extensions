/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.integration.websocket.inbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.websocket.core.WebSocketSessionListener;
import org.springframework.integration.websocket.support.SubProtocolHandlerResolver;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Gary Russell
 * @since 3.0
 *
 */
public class WebSocketMessageDrivenChannelAdapter extends AbstractEndpoint
		implements WebSocketHandler, BeanFactoryAware {

	private final List<WebSocketSessionListener> sessionListeners = new ArrayList<WebSocketSessionListener>();

	private final SubProtocolHandlerResolver subProtocolHandlerResolver;

	private final MessageChannel outputChannel;

	public WebSocketMessageDrivenChannelAdapter(SubProtocolHandlerResolver protocolHandlerResolver, MessageChannel outputChannel) {
		this.subProtocolHandlerResolver = protocolHandlerResolver;
		this.outputChannel = outputChannel;
	}

	public void onInit() {
		// TODO Requires bean factory to be a ListableBeanFactory
		for (Map.Entry<String, WebSocketSessionListener> entry : ((ListableBeanFactory)getBeanFactory()).getBeansOfType(WebSocketSessionListener.class).entrySet()) {
			sessionListeners.add(entry.getValue());
		}
	}

	@Override
	public String getComponentType(){
		return "websocket:inbound-channel-adapter";
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		for (WebSocketSessionListener sessionListener: sessionListeners) {
			sessionListener.sessionBegan(session);
		}

		this.subProtocolHandlerResolver.resolveSubProtocolHandler(session).afterSessionStarted(session, outputChannel);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> wsMessage) throws Exception {
		this.subProtocolHandlerResolver.resolveSubProtocolHandler(session).handleMessageFromClient(session, wsMessage, outputChannel);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		// TODO Remove the session?
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		this.subProtocolHandlerResolver.resolveSubProtocolHandler(session).afterSessionEnded(session, closeStatus, outputChannel);

		for (WebSocketSessionListener sessionListener: sessionListeners) {
			sessionListener.sessionEnded(session);
		}
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	@Override
	protected void doStart() {
	}

	@Override
	protected void doStop() {
	}
}
