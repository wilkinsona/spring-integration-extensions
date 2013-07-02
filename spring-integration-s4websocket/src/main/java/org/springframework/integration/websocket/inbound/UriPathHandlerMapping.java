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

package org.springframework.integration.websocket.inbound;

import org.springframework.integration.websocket.inbound.RequestHandlerFactoryBean.PathAware;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.AbstractDetectingUrlHandlerMapping;

/**
 * A {@link org.springframework.web.servlet.HandlerMapping} implementation that matches
 * against the value of the 'path' attribute, if present, on a Spring Integration WebSocket
 * &lt;inbound-channel-adapter&gt; or &lt;inbound-gateway&gt; element.
 *
 * @author Oleg Zhurakousky
 * @author Mark Fisher
 * @author Gary Russell
 * @since 3.0
 */
public class UriPathHandlerMapping extends AbstractDetectingUrlHandlerMapping {

	public UriPathHandlerMapping() {
		this.setDetectHandlersInAncestorContexts(true);
	}

	public UriPathHandlerMapping(int order) {
		this();
		this.setOrder(order);
	}

	@Override
	protected String[] determineUrlsForHandler(String beanName) {
		String[] urls = null;
		Class<?> beanClass = getApplicationContext().getType(beanName);
		if (PathAware.class.isAssignableFrom(beanClass)) {
			PathAware handler = getApplicationContext().getBean(beanName, PathAware.class);
			String path = handler.getPath();
			if (StringUtils.hasText(path)) {
				urls = new String[]{path};
			}
		}
		return urls;
	}

}
