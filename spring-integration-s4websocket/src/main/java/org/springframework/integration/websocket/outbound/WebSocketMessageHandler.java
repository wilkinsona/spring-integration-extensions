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

import org.springframework.integration.Message;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.websocket.support.WebSocketHeaders;
import org.springframework.util.Assert;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Gary Russell
 * @since 3.0
 *
 */
public class WebSocketMessageHandler extends AbstractMessageHandler {

	@Override
	protected void handleMessageInternal(Message<?> message) throws Exception {
		Assert.isInstanceOf(String.class, message.getPayload());
		WebSocketSession session = (WebSocketSession) message.getHeaders().get(WebSocketHeaders.WS_SESSION);
		Assert.notNull(session, "No session in message");
		// TODO: convert handle to session
		session.sendMessage(new TextMessage((String) message.getPayload()));
	}

	@Override
	public String getComponentType() {
		return "websocket:outbound-channel-adapter";
	}

}
