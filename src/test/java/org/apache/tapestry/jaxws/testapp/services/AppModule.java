package org.apache.tapestry.jaxws.testapp.services;

import org.apache.tapestry.jaxws.JAXWSModule;
import org.apache.tapestry.jaxws.Webservice;
import org.apache.tapestry.jaxws.util.SOAPLoggingHandler;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;

@SubModule(JAXWSModule.class)
public class AppModule
{
	public static void bind(final ServiceBinder binder)
	{
		binder.bind(TestWebservice.class);
	}

	public static void contributeMetroHttpServletRequestFilter(OrderedConfiguration<Webservice> configuration,
		TestWebservice testWebservice)
	{
		configuration.add("testws", new Webservice(TestWebservice.class, testWebservice, "TestWebservice", "/testws",
			new SOAPLoggingHandler("testws")));
	}
}
