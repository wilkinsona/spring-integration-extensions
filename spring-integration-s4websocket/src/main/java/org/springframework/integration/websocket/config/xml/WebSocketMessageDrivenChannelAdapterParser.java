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

import org.w3c.dom.Element;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractChannelAdapterParser;
import org.springframework.integration.config.xml.IntegrationNamespaceUtils;
import org.springframework.util.StringUtils;

/**
 * The WebSocket Message Driven Channel adapter parser
 *
 * @author Gary Russell
 * @since 3.0
 *
 */
public class WebSocketMessageDrivenChannelAdapterParser extends AbstractChannelAdapterParser {


	@Override
	protected AbstractBeanDefinition doParse(Element element, ParserContext parserContext, String channelName) {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder
				.genericBeanDefinition(WebSocketMessageDrivenChannelAdapterFactoryBean.class);
		builder.addPropertyValue("path", element.getAttribute("path"));
		builder.addPropertyReference("outputChannel", channelName);
		IntegrationNamespaceUtils.setValueIfAttributeDefined(builder, element, "sockjs");
		String sockjs = element.getAttribute("sockjs");
		if (StringUtils.hasText(sockjs) && "true".equalsIgnoreCase(sockjs)) {
			// TODO: make scheduler configurable instead of using the SI default
			builder.addPropertyReference("taskScheduler", "taskScheduler");
		}
		return builder.getBeanDefinition();
	}

}
