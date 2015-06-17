package org.safehaus.util;


import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;

import org.safehaus.model.JarvisContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JarvisRequestFilter implements ContainerRequestFilter
{
    private static Logger logger = LoggerFactory.getLogger( JarvisRequestFilter.class );
    private String cookieName;
    private String jiraUrl;


    public void setCookieName( final String cookieName )
    {
        this.cookieName = cookieName;
    }


    public void setJiraUrl( final String jiraUrl )
    {
        this.jiraUrl = jiraUrl;
    }


    @Override
    public void filter( final ContainerRequestContext requestContext ) throws IOException
    {
        logger.debug( String.format( "%s", requestContext.toString() ) );
        Cookie cookie = requestContext.getCookies().get( cookieName );

        if ( cookie != null )
        {
            JarvisContextHolder.setContext( new JarvisContext( jiraUrl, cookie ) );
        }
        else
        {
            JarvisContextHolder.setContext( new JarvisContext( jiraUrl ) );
        }
        logger.debug( requestContext.toString() );
    }
}
