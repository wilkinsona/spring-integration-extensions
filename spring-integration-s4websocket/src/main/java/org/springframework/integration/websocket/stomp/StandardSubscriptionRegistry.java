package org.springframework.integration.websocket.stomp;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.integration.websocket.core.WebSocketSessionListener;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketSession;

public class StandardSubscriptionRegistry implements SubscriptionRegistry, WebSocketSessionListener {

	private final Map<String, SessionSubscriptions> sessionSubscriptions = new ConcurrentHashMap<String, SessionSubscriptions>();

	@Override
	public void registerSubscription(String sessionId, String subscriptionId, String destination) {
		SessionSubscriptions sessionInfo = this.sessionSubscriptions.get(sessionId);
		Assert.notNull(sessionInfo, "Attempted to register subscription for unknown session");
		sessionInfo.addSubscription(destination, subscriptionId);
	}

	@Override
	public Set<String> getSubscriptions(String sessionId, String destination) {
		SessionSubscriptions sessionInfo = this.sessionSubscriptions.get(sessionId);
		Assert.notNull(sessionInfo, "Attempted to get subscriptions for unknown session");
		return sessionInfo.getSubscriptionsForDestination(destination);
	}

	@Override
	public void sessionBegan(WebSocketSession webSocketSession) {
		this.sessionSubscriptions.put(webSocketSession.getId(),  new SessionSubscriptions());
	}

	@Override
	public void sessionEnded(WebSocketSession webSocketSession) {
		this.sessionSubscriptions.remove(webSocketSession.getId());
	}

	private static final class SessionSubscriptions {

		private final MultiValueMap<String, String> subscriptions = new LinkedMultiValueMap<String, String>(4);

		public void addSubscription(String destination, String subscriptionId) {
			this.subscriptions.add(destination, subscriptionId);
		}

		public Set<String> getSubscriptionsForDestination(String destination) {
			List<String> ids = this.subscriptions.get(destination);
			return (ids != null) ? new HashSet<String>(ids) : null;
		}
	}

}
