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

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractChannelAdapterParser;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * The WebSocket Message Driven Channel adapter parser
 *
 * @author Gary Russell
 * @since 3.0
 *
 */
public class WebSocketMessageDrivenChannelAdapterParser extends AbstractChannelAdapterParser {

	private static final String CLASS_NAME_WEB_SOCKET_MESSAGE_DRIVEN_CHANNEL_ADAPTER = WebSocketNamespaceUtils.PACKAGE_NAME_PREFIX + "inbound.WebSocketMessageDrivenChannelAdapter";

	private static final String CLASS_NAME_REQUEST_HANDLER_FACTORY_BEAN = WebSocketNamespaceUtils.PACKAGE_NAME_PREFIX + "inbound.RequestHandlerFactoryBean";

	@Override
	protected AbstractBeanDefinition doParse(Element element, ParserContext parserContext, String channelName) {
		WebSocketNamespaceUtils.registerSessionRegistryIfNecessary(parserContext.getRegistry());

		BeanDefinitionBuilder adapterBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(CLASS_NAME_WEB_SOCKET_MESSAGE_DRIVEN_CHANNEL_ADAPTER);
		adapterBuilder.addPropertyReference("outputChannel", channelName);
		IntegrationNamespaceUtils.setReferenceIfAttributeDefined(adapterBuilder,  element,  "transformer");
		parserContext.getRegistry().registerBeanDefinition("webSocketMessageDrivenChannelAdapter", adapterBuilder.getBeanDefinition());

		BeanDefinitionBuilder requestHandlerBuilder = BeanDefinitionBuilder.genericBeanDefinition(CLASS_NAME_REQUEST_HANDLER_FACTORY_BEAN);
		requestHandlerBuilder.addPropertyReference("channelAdapter", "webSocketMessageDrivenChannelAdapter");
		IntegrationNamespaceUtils.setValueIfAttributeDefined(requestHandlerBuilder, element, "sockjs");
		requestHandlerBuilder.addPropertyValue("path", element.getAttribute("path"));

		String sockjs = element.getAttribute("sockjs");
		if (StringUtils.hasText(sockjs) && "true".equalsIgnoreCase(sockjs)) {
			// TODO: make scheduler configurable instead of using the SI default
			requestHandlerBuilder.addPropertyReference("taskScheduler", "taskScheduler");
		}
		return requestHandlerBuilder.getBeanDefinition();
	}

}