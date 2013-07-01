package org.springframework.integration.websocket.stomp;

import java.util.Set;

public interface SubscriptionRegistry {

	Set<String> getSubscriptions(String sessionId, String destination);

	void registerSubscription(String sessionId, String subscriptionId, String destination);

}
