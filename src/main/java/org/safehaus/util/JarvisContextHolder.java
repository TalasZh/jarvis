package org.safehaus.util;


import org.safehaus.model.JarvisContext;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JarvisContextHolder
{
    private static final ThreadLocal<JarvisContext> threadLocalScope = new ThreadLocal<>();


    public final static JarvisContext getContext()
    {
        return threadLocalScope.get();
    }


    public final static void setContext( JarvisContext securityContext )
    {
        threadLocalScope.set( securityContext );
    }
}
