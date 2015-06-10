package org.safehaus.stash.util;


import java.util.Map;

import javax.ws.rs.core.Cookie;

import org.restlet.data.Form;
import org.restlet.resource.ClientResource;
import org.safehaus.util.JarvisContextHolder;


public class RestUtil
{

    protected ClientResource getClientResource( String url )
    {
        ClientResource clientResource = new ClientResource( url );

        Cookie cookie = JarvisContextHolder.getContext().getCookie();
        clientResource.getRequest().getCookies().add( cookie.getName(), cookie.getValue() );

        return clientResource;
    }


    public String get( String url, Map<String, String> queryParams ) throws RestException
    {
        try
        {
            ClientResource clientResource = getClientResource( url );

            for ( Map.Entry<String, String> queryParam : queryParams.entrySet() )
            {
                clientResource.addQueryParameter( queryParam.getKey(), queryParam.getValue() );
            }

            return clientResource.get().getText();
        }
        catch ( Exception e )
        {
            throw new RestException( String.format( "Error GET'ing resource %s", url ), e );
        }
    }


    public String post( String url, Map<String, String> queryParams ) throws RestException
    {

        try
        {
            ClientResource clientResource = getClientResource( url );

            Form form = new Form();

            for ( Map.Entry<String, String> queryParam : queryParams.entrySet() )
            {
                form.add( queryParam.getKey(), queryParam.getValue() );
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
