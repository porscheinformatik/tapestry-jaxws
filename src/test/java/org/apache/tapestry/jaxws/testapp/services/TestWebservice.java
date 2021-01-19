package org.apache.tapestry.jaxws.testapp.services;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class TestWebservice
{
	@WebMethod
	public String echo(String value)
	{
		return value;
	}
}
