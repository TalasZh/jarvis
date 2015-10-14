package org.safehaus.model;


import javax.ws.rs.core.Cookie;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.util.AnonymousAuthenticationHandler;
import org.safehaus.util.CrowdAuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.apache.http.client.HttpClient;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.RestClient;
import net.rcarz.jiraclient.greenhopper.GreenHopperClient;


/**
 * Created by talas on 10/14/15.
 */
public class JiraContext
{
    private static Logger logger = LoggerFactory.getLogger( JiraContext.class );
    private Cookie cookie;
    private ICredentials authenticationHandler;
    private String jiraUrl;
    private JiraClient jiraSprintClient;


    public JiraContext( final String jiraUrl )
    {
        authenticationHandler = new AnonymousAuthenticationHandler();
        this.jiraUrl = jiraUrl;
    }


    public JiraContext( final String jiraUrl, ICredentials authenticationHandler )
    {
        this.authenticationHandler = authenticationHandler;
        this.jiraUrl = jiraUrl;
    }


    public JiraContext( final String jiraUrl, final Cookie cookie )
    {
        this.jiraUrl = jiraUrl;
        this.cookie = cookie;
        authenticationHandler = new CrowdAuthenticationHandler( cookie );
    }


    public JiraContext( final Cookie cookie )
    {
        this.cookie = cookie;
        authenticationHandler = new CrowdAuthenticationHandler( cookie );
    }


    public JiraContext( final String jiraUrl, final String username, final String password )
    {
        this.jiraUrl = jiraUrl;
        authenticationHandler = new BasicCredentials( username, password );
    }


    public JiraClient getJiraClient() throws JiraClientException
    {
        if ( jiraSprintClient == null )
        {
            try
            {
                jiraSprintClient = new JiraClient( jiraUrl, authenticationHandler );
            }
            catch ( JiraException e )
            {
                logger.error( "Error initializing jira rest client", e );
            }
        }
        return jiraSprintClient;
    }


    public GreenHopperClient getJiraSprintClient()
    {
        if ( jiraSprintClient == null )
        {
            try
            {
                jiraSprintClient = new JiraClient( jiraUrl, authenticationHandler );
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
        if ( jiraSprintClient != null )
        {
            logger.debug( "Destroying JIRA client..." );
            RestClient restClient = jiraSprintClient.getRestClient();
            if ( restClient != null )
            {
                HttpClient httpClient = restClient.getHttpClient();
                if ( httpClient.getConnectionManager() != null )
                {
                    httpClient.getConnectionManager().shutdown();
                }
            }

            jiraSprintClient = null;
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
