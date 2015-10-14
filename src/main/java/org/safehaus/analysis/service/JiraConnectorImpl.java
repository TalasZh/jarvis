package org.safehaus.analysis.service;


import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraRestClient;
import org.safehaus.model.JarvisContext;
import org.safehaus.model.JiraContext;
import org.safehaus.util.JarvisContextHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.rcarz.jiraclient.JiraClient;


/**
 * Created by kisik on 29.07.2015.
 */
public class JiraConnectorImpl implements JiraConnector
{
    private static final Log log = LogFactory.getLog( JiraConnectorImpl.class );
    private String jiraURL;
    private String jiraUserName;
    private String jiraPass;


    public JiraConnectorImpl( String jiraURL, String jiraUserName, String jiraPass )
    {
        this.jiraURL = jiraURL;
        this.jiraUserName = jiraUserName;
        this.jiraPass = jiraPass;
    }


    @Override
    public JiraRestClient jiraConnect() throws JiraClientException
    {
        log.info( "jiraConnect()" );
        JiraRestClient jiraRestClient = null;
        if ( JarvisContextHolder.getContext() != null && JarvisContextHolder.getContext().getJiraRestClient() != null )
        {
            jiraRestClient = JarvisContextHolder.getContext().getJiraRestClient();
        }
        else
        {
            JarvisContextHolder.setContext( new JarvisContext( jiraURL, jiraUserName, jiraPass ) );
            if ( JarvisContextHolder.getContext() != null
                    && JarvisContextHolder.getContext().getJiraRestClient() != null )
            {
                jiraRestClient = JarvisContextHolder.getContext().getJiraRestClient();
            }
            else
            {
                log.info( "JarvisContextHolder is null." );
                throw new JiraClientException( "Jira Client is null." );
            }
        }
        return jiraRestClient;
    }


    @Override
    public JiraClient getJiraClient() throws JiraClientException
    {
        JiraClient jiraRestClient;
        if ( JarvisContextHolder.getJiraContext() != null
                && JarvisContextHolder.getJiraContext().getJiraClient() != null )
        {
            jiraRestClient = JarvisContextHolder.getJiraContext().getJiraClient();
        }
        else
        {
            JarvisContextHolder.setJiraContext( new JiraContext( jiraURL, jiraUserName, jiraPass ) );
            if ( JarvisContextHolder.getJiraContext() != null
                    && JarvisContextHolder.getJiraContext().getJiraClient() != null )
            {
                jiraRestClient = JarvisContextHolder.getJiraContext().getJiraClient();
            }
            else
            {
                log.info( "JarvisContextHolder is null." );
                throw new JiraClientException( "Jira Client is null." );
            }
        }
        return jiraRestClient;
    }


    public void destroy()
    {
        if ( JarvisContextHolder.getJiraContext() != null )
        {
            JarvisContextHolder.getJiraContext().destroy();
            JarvisContextHolder.setJiraContext( null );
        }
    }
}
