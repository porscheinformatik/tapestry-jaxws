package org.apache.tapestry.jaxws.internal.metro;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.Handler;

import org.apache.tapestry.jaxws.Webservice;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.HttpServletRequestHandler;

import com.sun.xml.ws.api.BindingID;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.binding.BindingImpl;
import com.sun.xml.ws.server.EndpointFactory;
import com.sun.xml.ws.transport.http.DeploymentDescriptorParser.AdapterFactory;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import com.sun.xml.ws.transport.http.servlet.WSServletDelegate;

public class MetroHttpServletRequestFilter implements HttpServletRequestFilter
{
	private final ServletContext servletContext;

	private final WSServletDelegate wsServletDelegate;

	private final List<String> urlPatterns;

	public MetroHttpServletRequestFilter(final List<Webservice> webservices,
		final ApplicationGlobals applicationGlobals) throws MalformedURLException
	{
		this.servletContext = applicationGlobals.getServletContext();

		Container servletContainer = new ServletContainer(servletContext);
		AdapterFactory<ServletAdapter> adapterFactory = new ServletAdapterList(servletContext);
		urlPatterns = new ArrayList<String>();

		List<ServletAdapter> adapters = new ArrayList<ServletAdapter>();
		for (Webservice webservice : webservices)
		{
			WSBinding binding = BindingImpl.create(BindingID.parse(webservice.getWebserviceClass()));
			@SuppressWarnings("rawtypes")
			List<Handler> handlerChain = binding.getHandlerChain();
			handlerChain.addAll(webservice.getHandlerChain());
			binding.setHandlerChain(handlerChain);

			@SuppressWarnings("rawtypes")
			WSEndpoint endpoint = EndpointFactory.createEndpoint(
				webservice.getWebserviceClass(),
				false,
				new ServiceInvoker(webservice.getService()),
				null,
				null,
				servletContainer,
				binding,
				null,
				null,
				null,
				true);
			adapters.add(adapterFactory.createAdapter(webservice.getName(), webservice.getUrlPattern(), endpoint));
			urlPatterns.add(webservice.getUrlPattern());
		}
		wsServletDelegate = new WSServletDelegate(adapters, servletContext);
	}

	public boolean service(HttpServletRequest request, HttpServletResponse response, HttpServletRequestHandler handler)
		throws IOException
	{
		String path = request.getServletPath();
		String pathInfo = request.getPathInfo();

		if (pathInfo != null)
			path += pathInfo;

		for (String urlPattern : urlPatterns)
		{
			if (path.startsWith(urlPattern))
			{
				try
				{
					wsServletDelegate.doPost(request, response, servletContext);
					return true;
				}
				catch (ServletException e)
				{
					return false;
				}
			}
		}

		// Not a match, so let it go.

		return handler.service(request, response);
	}
}
