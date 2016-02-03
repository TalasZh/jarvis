package org.safehaus.util;


import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JarvisResponseFilter implements ContainerResponseFilter
{
    private static Logger logger = LoggerFactory.getLogger( JarvisResponseFilter.class );

    @Override
    public void filter( final ContainerRequestContext requestContext, final ContainerResponseContext responseContext )
            throws IOException
    {
        JarvisContextHolder.getContext().destroy();
    }
}
