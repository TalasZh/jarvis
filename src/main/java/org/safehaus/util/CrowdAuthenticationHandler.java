package org.safehaus.util;


import javax.ws.rs.core.Cookie;

import org.apache.http.HttpRequest;

import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestClient;


/**
 * Created by tzhamakeev on 5/26/15.
 */
public class CrowdAuthenticationHandler implements ICredentials
{
    private Cookie cookie;


    public CrowdAuthenticationHandler( final Cookie cookie )
    {
        this.cookie = cookie;
    }


    @Override
    public void initialize( final RestClient client ) throws JiraException
    {

    }


    @Override
    public void authenticate( final HttpRequest httpRequest )
    {
        httpRequest.addHeader( "Cookie", String.format( "%s=%s", cookie.getName(), cookie.getValue() ) );
    }


    @Override
    public String getLogonName()
    {
        return null;
    }


    @Override
    public void logout( final RestClient client ) throws JiraException
    {

    }
}
