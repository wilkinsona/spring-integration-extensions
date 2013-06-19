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

package org.springframework.integration.websocket.outbound;

import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.websocket.core.SessionRegistry;
import org.springframework.integration.websocket.support.WebSocketHeaders;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketMessage;

/**
 * @author Gary Russell
 * @since 3.0
 *
 */
public class WebSocketMessageHandler extends AbstractMessageHandler {

	private final SessionRegistry sessionManager;

	private volatile WebSocketMessageOutboundTransformer transformer = new DefaultWebSocketMessageOutboundTransformer();

	public WebSocketMessageHandler(SessionRegistry sessionManager) {
		this.sessionManager = sessionManager;
	}

	@Override
	protected void handleMessageInternal(Message<?> message) throws Exception {
		String sessionId = (String) message.getHeaders().get(WebSocketHeaders.WS_SESSION_ID);
		Assert.notNull(sessionId, "No sessionId in message");

		WebSocketMessage<?> wsMessage = transformer.transform(message);

		this.sessionManager.getSession(sessionId).sendMessage(wsMessage);
	}

	@Override
	public String getComponentType() {
		return "websocket:outbound-channel-adapter";
	}

	public void setTransformer(WebSocketMessageOutboundTransformer transformer) {
		Assert.notNull(transformer, "transformer must not be null");
		this.transformer = transformer;
	}

}
