package org.safehaus.model;


import javax.ws.rs.core.Cookie;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraRestClient;
import org.safehaus.jira.JiraRestClientImpl;
import org.safehaus.util.AnonymousAuthenticationHandler;
import org.safehaus.util.CrowdAuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.greenhopper.GreenHopperClient;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JarvisContext
{
    private static Logger logger = LoggerFactory.getLogger( JarvisContext.class );
    private Cookie cookie;
    private ICredentials authenticationHandler;
    private String jiraUrl;
    private JiraRestClient jiraRestClient;
    private net.rcarz.jiraclient.JiraClient jiraSprintClient;


    public JarvisContext( final String jiraUrl )
    {
        authenticationHandler = new AnonymousAuthenticationHandler();
        this.jiraUrl = jiraUrl;
    }


    public JarvisContext( final String jiraUrl, ICredentials authenticationHandler )
    {
        this.authenticationHandler = authenticationHandler;
        this.jiraUrl = jiraUrl;
    }


    public JarvisContext( final String jiraUrl, final Cookie cookie )
    {
        this.jiraUrl = jiraUrl;
        this.cookie = cookie;
        authenticationHandler = new CrowdAuthenticationHandler( cookie );
    }


    public JarvisContext( final Cookie cookie )
    {
        this.cookie = cookie;
        authenticationHandler = new CrowdAuthenticationHandler( cookie );
    }


    public JarvisContext( final String jiraUrl, final String username, final String password )
    {
        this.jiraUrl = jiraUrl;
        authenticationHandler = new BasicCredentials( username, password );
    }


    public JiraRestClient getJiraRestClient() throws JiraClientException
    {
        if ( jiraRestClient == null )
        {
            logger.debug(
                    String.format( "Creating JIRA client using [%s]...", authenticationHandler.getClass().getName() ) );
            jiraRestClient = new JiraRestClientImpl( jiraUrl, authenticationHandler );
        }

        return jiraRestClient;
    }


    public GreenHopperClient getJiraSprintClient()
    {
        if ( jiraSprintClient == null )
        {
            try
            {
                jiraSprintClient = new net.rcarz.jiraclient.JiraClient( jiraUrl, authenticationHandler );
            }
            catch ( JiraException e )
            {
                logger.error( "Error initializing jira rest client", e );
            }
        }

        return new GreenHopperClient( jiraSprintClient );
    }


    public void destroy()
    {
        if ( jiraRestClient != null )
        {
            logger.debug( "Destoying JIRA client..." );
        }
    }


    public UserDetails getUserDetails()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( !( auth instanceof AnonymousAuthenticationToken ) )
        {
            UserDetails userDetails = ( UserDetails ) auth.getPrincipal();
            logger.debug( auth.getCredentials() != null ? auth.getCredentials().toString() : "Credentials not found." );
            logger.debug( userDetails.getUsername() );
            return userDetails;
        }
        else
        {
            return null;
        }
    }


    public Cookie getCookie()
    {
        return cookie;
    }
}
