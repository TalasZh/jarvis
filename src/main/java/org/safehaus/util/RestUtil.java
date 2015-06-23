package org.safehaus.util;


import java.util.Map;

import javax.ws.rs.core.Cookie;

import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.resource.ClientResource;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;


public class RestUtil
{
    //auth cookie
    private Cookie authCookie;
    //-------------
    //user credentials
    private String username;
    private String password;


    public RestUtil( final Cookie authCookie )
    {
        Preconditions.checkNotNull( authCookie );

        this.authCookie = authCookie;
    }


    public RestUtil( final String username, final String password )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( username ) );
        Preconditions.checkArgument( !Strings.isNullOrEmpty( password ) );

        this.username = username;
        this.password = password;
    }


    protected ClientResource getClientResource( String url )
    {
        ClientResource clientResource = new ClientResource( url );

        if ( authCookie != null )
        {
            clientResource.getRequest().getCookies().add( authCookie.getName(), authCookie.getValue() );
        }
        else
        {
            clientResource.setChallengeResponse( ChallengeScheme.HTTP_BASIC, username, password );
        }

        return clientResource;
    }


    public String get( String url, Map<String, String> queryParams ) throws RestException
    {
        try
        {
            ClientResource clientResource = getClientResource( url );

            if ( queryParams != null )
            {
                for ( Map.Entry<String, String> queryParam : queryParams.entrySet() )
                {
                    clientResource.addQueryParameter( queryParam.getKey(), queryParam.getValue() );
                }
            }

            return clientResource.get().getText();
        }
        catch ( Exception e )
        {
            throw new RestException( String.format( "Error GET'ing resource %s", url ), e );
        }
    }


    public String post( String url, Map<String, String> formParams ) throws RestException
    {

        try
        {
            ClientResource clientResource = getClientResource( url );

            Form form = new Form();

            if ( formParams != null )
            {

                for ( Map.Entry<String, String> formParam : formParams.entrySet() )
                {
                    form.add( formParam.getKey(), formParam.getValue() );
                }
            }

            return clientResource.post( form ).getText();
        }
        catch ( Exception e )
        {
            throw new RestException( String.format( "Error POST'ing resource %s", url ), e );
        }
    }


    public static class RestException extends Exception
    {

        public RestException( final String message, final Throwable cause )
        {
            super( message, cause );
        }
    }
}
