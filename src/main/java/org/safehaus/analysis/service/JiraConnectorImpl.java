package org.safehaus.analysis.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraClient;
import org.safehaus.model.JarvisContext;
import org.safehaus.util.JarvisContextHolder;


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
    public JiraClient jiraConnect() throws JiraClientException
    {
        log.info( "jiraConnect()" );
        JiraClient jiraClient = null;
        if ( JarvisContextHolder.getContext() != null && JarvisContextHolder.getContext().getJiraClient() != null )
        {
            jiraClient = JarvisContextHolder.getContext().getJiraClient();
        }
        else
        {
            JarvisContextHolder.setContext( new JarvisContext( jiraURL, jiraUserName, jiraPass ) );
            if ( JarvisContextHolder.getContext() != null && JarvisContextHolder.getContext().getJiraClient() != null )
            {
                jiraClient = JarvisContextHolder.getContext().getJiraClient();
            }
            else
            {
                log.info( "JarvisContextHolder is null." );
                throw new JiraClientException( "Jira Client is null." );
            }

        }
        return jiraClient;
    }
}
