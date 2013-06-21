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
package org.springframework.integration.websocket.config.xml;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.websocket.inbound.WebSocketMessageDrivenChannelAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;
import org.springframework.web.socket.sockjs.SockJsService;
import org.springframework.web.socket.sockjs.support.DefaultSockJsService;
import org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler;

/**
 * FactoryBean that wraps the {@link WebSocketMessageDrivenChannelAdapter} in an appropriate
 * handler.
 *
 * @author Gary Russell
 * @since 3.0
 *
 */
public class WebSocketMessageDrivenChannelAdapterFactoryBean extends AbstractFactoryBean<Object> {

	private volatile MessageChannel outputChannel;

	private volatile String path;

	private volatile boolean sockjs;

	private volatile TaskScheduler taskScheduler;

	public void setOutputChannel(MessageChannel outputChannel) {
		this.outputChannel = outputChannel;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSockjs(boolean sockjs) {
		this.sockjs = sockjs;
	}

	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO: different wrapper when running outside a container
		if (this.sockjs) {
			return PathAwareSockJsHttpRequestHandler.class;
		}
		else {
			return PathAwareWebSocketHttpRequestHandler.class;
		}
	}

	@Override
	protected Object createInstance() throws Exception {
		Assert.hasText(this.path, "'path' must not be null or empty");
		WebSocketMessageDrivenChannelAdapter webSocketHandler = new WebSocketMessageDrivenChannelAdapter();
		webSocketHandler.setOutputChannel(this.outputChannel);
		if (this.sockjs) {
			Assert.notNull(this.taskScheduler, "'taskScheduler' must be provided");
			SockJsService sockJsService = new DefaultSockJsService(this.taskScheduler);
			PathAwareSockJsHttpRequestHandler handler = new PathAwareSockJsHttpRequestHandler(sockJsService,
					webSocketHandler, this.path);
			return handler;
		}
		else {
			PathAwareWebSocketHttpRequestHandler handler = new PathAwareWebSocketHttpRequestHandler(webSocketHandler,
					this.path);
			return handler;
		}
	}

	public interface PathAware {

		String getPath();

	}

	public class PathAwareWebSocketHttpRequestHandler extends WebSocketHttpRequestHandler
		implements PathAware {

		private final String path;

		public PathAwareWebSocketHttpRequestHandler(WebSocketHandler webSocketHandler, HandshakeHandler handshakeHandler,
					String path) {
			super(webSocketHandler, handshakeHandler);
			this.path = path;
		}

		public PathAwareWebSocketHttpRequestHandler(WebSocketHandler webSocketHandler,
					String path) {
			super(webSocketHandler);
			this.path = path;
		}

		public String getPath() {
			return path;
		}

	}

	public class PathAwareSockJsHttpRequestHandler extends SockJsHttpRequestHandler
		implements PathAware {

		private final String path;

		public PathAwareSockJsHttpRequestHandler(SockJsService sockJsService, WebSocketHandler webSocketHandler,
				String path) {
			super(sockJsService, webSocketHandler);
			this.path = path;
		}

		public String getPath() {
			return path;
		}

	}
}
