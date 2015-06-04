package org.safehaus.util;


import javax.ws.rs.core.Cookie;

import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;


/**
 * Created by tzhamakeev on 5/26/15.
 */
public class CrowdAuthenticationHandler implements AuthenticationHandler
{
    private Cookie cookie;


    public CrowdAuthenticationHandler( final Cookie cookie )
    {
        this.cookie = cookie;
    }


    @Override
    public void configure( final Request request )
    {
        request.setHeader( "Cookie", String.format( "%s=%s", cookie.getName(), cookie.getValue() ) );
    }
}
