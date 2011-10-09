package org.apache.tapestry.jaxws.internal.metro;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceException;

import com.sun.xml.ws.api.server.BoundEndpoint;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.transport.http.servlet.ServletModule;

public class ServletContainer extends Container
{
	private final ServletContext servletContext;

	private final ServletModule module = new ServletModule()
	{
		private final List<BoundEndpoint> endpoints = new ArrayList<BoundEndpoint>();

		public List<BoundEndpoint> getBoundEndpoints()
		{
			return endpoints;
		}

		public String getContextPath()
		{
			// Cannot compute this since we don't know about hostname and port etc
			throw new WebServiceException("Container " + ServletContainer.class.getName()
				+ " doesn't support getContextPath()");
		}
	};

	private final com.sun.xml.ws.api.ResourceLoader loader = new com.sun.xml.ws.api.ResourceLoader()
	{
		public URL getResource(String resource) throws MalformedURLException
		{
			return servletContext.getResource("/WEB-INF/" + resource);
		}
	};

	public ServletContainer(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}

	public <T> T getSPI(Class<T> spiType)
	{
		if (spiType == ServletContext.class)
		{
			return spiType.cast(servletContext);
		}
		if (spiType.isAssignableFrom(ServletModule.class))
		{
			return spiType.cast(module);
		}
		if (spiType == com.sun.xml.ws.api.ResourceLoader.class)
		{
			return spiType.cast(loader);
		}
		return null;
	}
}