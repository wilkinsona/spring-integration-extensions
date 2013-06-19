package org.springframework.integration.websocket.config.xml;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

final class WebSocketNamespaceUtils {

	static final String PACKAGE_NAME_PREFIX = "org.springframework.integration.websocket.";

	static final String BEAN_NAME_SESSION_REGISTRY = "webSocketSessionManager";

	private static final String CLASS_NAME_STANDARD_SESSION_REGISTRY = WebSocketNamespaceUtils.PACKAGE_NAME_PREFIX + "core.StandardSessionRegistry";

	private WebSocketNamespaceUtils() {

	}

	static String registerSessionRegistryIfNecessary(BeanDefinitionRegistry registry) {
		if (!registry.containsBeanDefinition(BEAN_NAME_SESSION_REGISTRY)) {
			BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(CLASS_NAME_STANDARD_SESSION_REGISTRY).getBeanDefinition();
			registry.registerBeanDefinition(BEAN_NAME_SESSION_REGISTRY, beanDefinition);
		}
		return BEAN_NAME_SESSION_REGISTRY;
	}
}
