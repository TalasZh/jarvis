package org.safehaus.jira;


import org.apache.http.HttpRequest;

import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestClient;


/**
 * Created by ermek on 8/20/15.
 */
public class CookieAuth implements ICredentials
{
    private final String TOKEN;


    public CookieAuth( final String token )
    {
        TOKEN = token;
    }


    @Override
    public void initialize( final RestClient client ) throws JiraException
    {

    }


    @Override
    public void authenticate( final HttpRequest httpRequest )
    {
        httpRequest.addHeader( "Cookie", String.format( "crowd.token_key=%s", TOKEN ) );
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
