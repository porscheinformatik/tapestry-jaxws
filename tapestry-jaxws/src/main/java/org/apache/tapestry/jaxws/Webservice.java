package org.apache.tapestry.jaxws;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.ws.handler.Handler;

public class Webservice
{
	private final Class<?> webserviceClass;

	private final Object service;

	private final String name;

	private final String urlPattern;

	@SuppressWarnings("rawtypes")
	private final List<Handler> handlerChain;

	@SuppressWarnings("rawtypes")
	public Webservice(Class<?> webserviceClass, Object service, String name, String urlPattern, Handler... handlerChain)
	{
		super();
		this.webserviceClass = webserviceClass;
		this.service = service;
		this.name = name;
		this.urlPattern = urlPattern;

		if (handlerChain == null)
			this.handlerChain = Collections.emptyList();
		else
			this.handlerChain = Arrays.asList(handlerChain);
	}

	public Class<?> getWebserviceClass()
	{
		return webserviceClass;
	}

	public Object getService()
	{
		return service;
	}

	public String getName()
	{
		return name;
	}

	public String getUrlPattern()
	{
		return urlPattern;
	}

	@SuppressWarnings("rawtypes")
	public List<Handler> getHandlerChain()
	{
		return handlerChain;
	}
}
