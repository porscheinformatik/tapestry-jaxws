package org.apache.tapestry.jaxws.testapp.services;

import com.porscheinformatik.tapestry.jaxws.JAXWSModule;
import com.porscheinformatik.tapestry.jaxws.Webservice;
import com.porscheinformatik.tapestry.jaxws.util.SOAPLoggingHandler;
import org.apache.tapestry5.commons.OrderedConfiguration;
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
