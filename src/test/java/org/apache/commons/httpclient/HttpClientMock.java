/**
 * foo.
 */

package org.apache.commons.httpclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class HttpClientMock extends HttpClient
{
	private int expectedResponseStatus;
	private String expectedResponseBody;

	public HttpClientMock(int responseStatus, String responseBody)
	{
		this.expectedResponseStatus = responseStatus;
		this.expectedResponseBody = responseBody;
	}

	@Override
	public int executeMethod(HttpMethod method) throws IOException
	{
		((HttpMethodBase) method).setResponseStream(new ByteArrayInputStream(
				expectedResponseBody.getBytes("UTF-8")));
		return expectedResponseStatus;
	}

	public void setExpectedResponseStatus(int expectedResponseStatus)
	{
		this.expectedResponseStatus = expectedResponseStatus;
	}
	
	public void setExpectedResponseBody(String expectedResponseBody)
	{
		this.expectedResponseBody = expectedResponseBody;
	}
}
