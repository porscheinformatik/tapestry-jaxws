# Tapestry JAX-WS Integration

This Tapestry module provides integration of JAXWS to Tapestry web applications.

## Features

 * Using Tapestry services as web services
 * JAX-WS API 2.2
 * SOAPLoggingHandler for easy logging of requests/responses

## Usage
 
You can easily add a contribution to the MetroHttpServletRequestFilter:
 
	public static void contributeMetroHttpServletRequestFilter(OrderedConfiguration<Webservice> configuration,
		TestWebservice testWebservice)
	{
		configuration.add("testws", new Webservice(TestWebservice.class, testWebservice, "TestWebservice", "/testws",
			new SOAPLoggingHandler("testws")));
	}

The Webservice constructor takes the following parameters:

 * the web service class (containing the JAX-WS annotations)
 * the web service instance (or Tapestry service, since this will be a proxy)
 * the name used in 
 * the URL pattern like "/webservice" or "/ws/myws"
 * and a list of handlers to be added
 
## Developer Info

This project is built with Maven - to generate the jar run: `mvn package`

Continuous builds are running on GitHub Actions: [![Build Status](https://github.com/porscheinformatik/tapestry-jaxws/workflows/Build/badge.svg)](https://github.com/porscheinformatik/tapestry-jaxws/actions)
