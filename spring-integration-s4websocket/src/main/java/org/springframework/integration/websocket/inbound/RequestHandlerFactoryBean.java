package org.springframework.integration.websocket.inbound;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.WebSocketHttpRequestHandler;
import org.springframework.web.socket.sockjs.SockJsService;
import org.springframework.web.socket.sockjs.support.DefaultSockJsService;
import org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler;

public class RequestHandlerFactoryBean extends AbstractFactoryBean<Object>{

	private volatile String path;

	private volatile boolean sockjs;

	private volatile TaskScheduler taskScheduler;

	private volatile WebSocketMessageDrivenChannelAdapter channelAdapter;

	public void setPath(String path) {
		this.path = path;
	}

	public void setSockjs(boolean sockjs) {
		this.sockjs = sockjs;
	}

	public void setTaskScheduler(TaskScheduler taskScheduler) {
		this.taskScheduler = taskScheduler;
	}

	public void setChannelAdapter(WebSocketMessageDrivenChannelAdapter channelAdapter) {
		this.channelAdapter = channelAdapter;
	}

	@Override
	protected Object createInstance() throws Exception {
		Assert.hasText(this.path, "'path' must not be null or empty");

		if (this.sockjs) {
			Assert.notNull(this.taskScheduler, "'taskScheduler' must be provided");
			SockJsService sockJsService = new DefaultSockJsService(this.taskScheduler);
			PathAwareSockJsHttpRequestHandler handler = new PathAwareSockJsHttpRequestHandler(sockJsService,
					this.channelAdapter, this.path);
			return handler;
		}
		else {
			PathAwareWebSocketHttpRequestHandler handler = new PathAwareWebSocketHttpRequestHandler(this.channelAdapter,
					this.path);
			return handler;
		}
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
