package org.safehaus.util;


import org.safehaus.model.ConfluenceContext;
import org.safehaus.model.JarvisContext;
import org.safehaus.model.JiraContext;
import org.safehaus.model.SonarContext;
import org.safehaus.model.StashContext;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JarvisContextHolder
{
    private static final ThreadLocal<JarvisContext> threadLocalScope = new ThreadLocal<>();

    private static final ThreadLocal<JiraContext> jiraContextThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<StashContext> stashContextThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<SonarContext> sonarContextThreadLocal = new ThreadLocal<>();

    private static final ThreadLocal<ConfluenceContext> confluenceContextThreadLocal = new ThreadLocal<>();


    public static JarvisContext getContext()
    {
        return threadLocalScope.get();
    }


    public static void setContext( JarvisContext securityContext )
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


    public static JiraContext getJiraContext()
    {
        return jiraContextThreadLocal.get();
    }


    public static void setJiraContext( JiraContext stashContext )
    {
        jiraContextThreadLocal.set( stashContext );
    }


    public static SonarContext getSonarContext()
    {
        return sonarContextThreadLocal.get();
    }


    public static void setSonarContext( SonarContext sonarContext )
    {
        sonarContextThreadLocal.set( sonarContext );
    }


    public static ConfluenceContext getConfluenceContext()
    {
        return confluenceContextThreadLocal.get();
    }


    public static void setConfluenceContext( ConfluenceContext confluenceContext )
    {
        confluenceContextThreadLocal.set( confluenceContext );
    }
}
