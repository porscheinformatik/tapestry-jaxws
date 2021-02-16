package com.porscheinformatik.tapestry.jaxws.internal.metro;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.server.Invoker;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.api.server.WSWebServiceContext;

public class ServiceInvoker extends Invoker
{
	private final Object service;

	public ServiceInvoker(Object service)
	{
		this.service = service;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void start(WSWebServiceContext wsc, WSEndpoint endpoint)
	{
		// nothing
	}

	@Override
	public Object invoke(Packet p, Method m, Object... args) throws InvocationTargetException, IllegalAccessException
	{
		return m.invoke(service, args);
	}
}