package org.springframework.integration.websocket.support;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.websocket.inbound.WebSocketMessageDrivenChannelAdapter;
import org.springframework.integration.websocket.outbound.WebSocketMessageHandler;
import org.springframework.messaging.handler.websocket.SubProtocolHandler;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.WebSocketSession;

/**
 * Helper class for resolving a {@link SubProtocolHandler} from a {@link
 * WebSocketSession}. Used for both inbound ({@link
 * WebSocketMessageDrivenChannelAdapter}) and outbound ({@link
 * WebSocketMessageHandler}) processing.
 *
 * @author Andy Wilkinson
 *
 */
public final class SubProtocolHandlerResolver {

	private final Log logger = LogFactory.getLog(SubProtocolHandlerResolver.class);

	private final Map<String, SubProtocolHandler> protocolHandlers =
			new TreeMap<String, SubProtocolHandler>(String.CASE_INSENSITIVE_ORDER);

	private final SubProtocolHandler defaultProtocolHandler;

	public SubProtocolHandlerResolver(List<SubProtocolHandler> protocolHandlers,
			SubProtocolHandler defaultProtocolHandler) {
		for (SubProtocolHandler handler: protocolHandlers) {
			List<String> protocols = handler.getSupportedProtocols();
			if (CollectionUtils.isEmpty(protocols)) {
				logger.warn("No sub-protocols, ignoring handler " + handler);
				continue;
			}
			for (String protocol: protocols) {
				SubProtocolHandler replaced = this.protocolHandlers.put(protocol, handler);
				if (replaced != null) {
					throw new IllegalStateException("Failed to map handler " + handler
							+ " to protocol '" + protocol + "', it is already mapped to handler " + replaced);
				}
			}
		}
		if ((this.protocolHandlers.size() == 1) &&(defaultProtocolHandler == null)) {
			this.defaultProtocolHandler = this.protocolHandlers.values().iterator().next();
		} else {
			this.defaultProtocolHandler = defaultProtocolHandler;
		}
	}

	/**
	 * Resolves the {@link SubProtocolHandler} for the given {@code session} using
	 * its {@link WebSocketSession#getAcceptedProtocol() accepted sub-protocol}.
	 *
	 * @param session The session to resolve the sub-protocol handler for
	 *
	 * @return The sub-protocol handler
	 *
	 * @throws IllegalStateException if a protocol handler cannot be resolved
	 */
	public SubProtocolHandler resolveSubProtocolHandler(WebSocketSession session) {
		SubProtocolHandler handler;
		String protocol = session.getAcceptedProtocol();
		if (protocol != null) {
			handler = this.protocolHandlers.get(protocol);
			Assert.state(handler != null,
					"No handler for sub-protocol '" + protocol + "', handlers=" + this.protocolHandlers);
		}
		else {
			handler = this.defaultProtocolHandler;
			Assert.state(handler != null,
					"No sub-protocol was requested and a default sub-protocol handler was not configured");
		}
		return handler;
	}
}
