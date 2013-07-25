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

package org.springframework.integration.websocket.stomp;

import org.springframework.integration.websocket.core.WebSocketSessionListener;
import org.springframework.messaging.simp.handler.MutableUserQueueSuffixResolver;
import org.springframework.web.socket.WebSocketSession;

public class UserQueueSuffixManagingWebSocketSessionListener implements WebSocketSessionListener {

	private final MutableUserQueueSuffixResolver queueSuffixResolver;

	public UserQueueSuffixManagingWebSocketSessionListener(MutableUserQueueSuffixResolver queueSuffixResolver) {
		this.queueSuffixResolver = queueSuffixResolver;
	}

	@Override
	public void sessionBegan(WebSocketSession session) {
		if (session.getPrincipal() != null) {
			this.queueSuffixResolver.addQueueSuffix(getUser(session), getSessionId(session), getQueueSuffix(session));
		}
	}

	@Override
	public void sessionEnded(WebSocketSession session) {
		if (session.getPrincipal() != null) {
			this.queueSuffixResolver.removeQueueSuffix(getUser(session), getSessionId(session));
		}
	}

	private String getUser(WebSocketSession session) {
		return session.getPrincipal().getName();
	}

	private String getSessionId(WebSocketSession session) {
		return session.getId();
	}

	private String getQueueSuffix(WebSocketSession session) {
		return session.getId();
	}
}
