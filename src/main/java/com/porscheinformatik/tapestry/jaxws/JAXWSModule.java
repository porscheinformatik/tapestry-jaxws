package com.porscheinformatik.tapestry.jaxws;

import com.porscheinformatik.tapestry.jaxws.internal.metro.MetroHttpServletRequestFilter;
import org.apache.tapestry5.commons.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.http.services.HttpServletRequestFilter;

public class JAXWSModule
{
	public static void bind(final ServiceBinder binder)
	{
		// TODO make this configurable
		System.setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace", "false");
		binder.bind(HttpServletRequestFilter.class, MetroHttpServletRequestFilter.class).withId(
			"MetroHttpServletRequestFilter");
	}

	public static void contributeHttpServletRequestHandler(
		OrderedConfiguration<HttpServletRequestFilter> configuration,
		@InjectService("MetroHttpServletRequestFilter") HttpServletRequestFilter jaxWsHttpServletRequestFilter)
	{
		configuration.add("jaxws", jaxWsHttpServletRequestFilter, "after:*");
	}
}
