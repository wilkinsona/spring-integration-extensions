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

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class SubProtocolHandlerResolverParser {

	private static final String CLASS_NAME_PROTOCOL_HANDLER_RESOLVER = "org.springframework.integration.websocket.support.SubProtocolHandlerResolver";

	@SuppressWarnings("rawtypes")
	AbstractBeanDefinition parseElement(Element element, ParserContext parserContext) {
		BeanDefinitionBuilder protocolHandlerResolverBuilder = BeanDefinitionBuilder.genericBeanDefinition(CLASS_NAME_PROTOCOL_HANDLER_RESOLVER);

		Element protocolHandlersElement = DomUtils.getChildElementByTagName(element, "protocol-handlers");
		ManagedList protocolHandlers = null;
		if (protocolHandlersElement != null) {
			protocolHandlers = parseProtocolHandlers(protocolHandlersElement, parserContext);
		}
		if (protocolHandlers == null) {
			protocolHandlers = new ManagedList();
		}

		protocolHandlerResolverBuilder.addConstructorArgValue(protocolHandlers);

		if (element.hasAttribute("default-protocol-handler")) {
			protocolHandlerResolverBuilder.addConstructorArgReference(element.getAttribute("default-protocol-handler"));
		} else {
			protocolHandlerResolverBuilder.addConstructorArgValue(null);
		}

		return protocolHandlerResolverBuilder.getBeanDefinition();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ManagedList parseProtocolHandlers(Element protocolHandlersElement, ParserContext parserContext) {
		ManagedList inboundProtocolHandlers = new ManagedList();
		NodeList childNodes = protocolHandlersElement.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				Element childElement = (Element) child;
				String localName = child.getLocalName();
				if ("bean".equals(localName)) {
					BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
					BeanDefinitionHolder holder = delegate.parseBeanDefinitionElement(childElement);
					holder = delegate.decorateBeanDefinitionIfRequired(childElement, holder);
					parserContext.registerBeanComponent(new BeanComponentDefinition(holder));
					inboundProtocolHandlers.add(new RuntimeBeanReference(holder.getBeanName()));
				}
				else if ("ref".equals(localName)) {
					String ref = childElement.getAttribute("bean");
					inboundProtocolHandlers.add(new RuntimeBeanReference(ref));
				}
			}
		}
		return inboundProtocolHandlers;
	}
}
