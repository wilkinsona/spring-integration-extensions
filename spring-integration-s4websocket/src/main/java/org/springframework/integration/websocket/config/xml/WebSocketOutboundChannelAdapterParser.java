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
import org.springframework.integration.config.xml.AbstractOutboundChannelAdapterParser;
import org.w3c.dom.Element;

/**
 * The parser for the WebSocket Outbound Channel Adapter.
 *
 * @author Gary Russell
 * @since 3.0
 *
 */
public class WebSocketOutboundChannelAdapterParser extends AbstractOutboundChannelAdapterParser {

	private static final String CLASS_NAME_WEB_SOCKET_MESSAGE_HANDLER = "org.springframework.integration.websocket.outbound.WebSocketMessageHandler";

	@Override
	protected boolean shouldGenerateId() {
		return false;
	}

	@Override
	protected boolean shouldGenerateIdAsFallback() {
		return true;
	}

	@Override
	protected AbstractBeanDefinition parseConsumer(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CLASS_NAME_WEB_SOCKET_MESSAGE_HANDLER);
		builder.addConstructorArgReference(WebSocketNamespaceUtils.registerSessionRegistryIfNecessary(parserContext.getRegistry()));
		builder.addConstructorArgValue(new SubProtocolHandlerResolverParser().parseElement(element, parserContext));
		return builder.getBeanDefinition();
	}
}
