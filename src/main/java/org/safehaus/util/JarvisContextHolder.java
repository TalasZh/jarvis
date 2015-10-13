package org.safehaus.util;


import org.safehaus.model.JarvisContext;
import org.safehaus.model.StashContext;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JarvisContextHolder
{
    private static final ThreadLocal<JarvisContext> threadLocalScope = new ThreadLocal<>();

    private static final ThreadLocal<StashContext> stashContextThreadLocal = new ThreadLocal<>();


    public final static JarvisContext getContext()
    {
        return threadLocalScope.get();
    }


    public final static void setContext( JarvisContext securityContext )
    {
        threadLocalScope.set( securityContext );
    }


    public static StashContext getStashContext()
    {
        return stashContextThreadLocal.get();
    }


    public static void setStashContext( StashContext stashContext )
    {
        stashContextThreadLocal.set( stashContext );
    }
}
