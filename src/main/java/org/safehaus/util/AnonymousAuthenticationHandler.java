package org.safehaus.util;


import org.apache.http.HttpRequest;

import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestClient;


/**
 * Created by talas on 9/13/15.
 */
public class AnonymousAuthenticationHandler implements ICredentials
{
    @Override
    public void initialize( final RestClient client ) throws JiraException
    {

    }


    @Override
    public void authenticate( final HttpRequest req )
    {

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
