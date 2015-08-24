package org.safehaus.model;


import java.io.IOException;

import javax.ws.rs.core.Cookie;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.CookieAuth;
import org.safehaus.jira.JiraClient;
import org.safehaus.jira.JiraClientImpl;
import org.safehaus.util.CrowdAuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;

import net.rcarz.jiraclient.greenhopper.GreenHopperClient;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JarvisContext
{
    private static Logger logger = LoggerFactory.getLogger( JarvisContext.class );
    private Cookie cookie;
    private AuthenticationHandler authenticationHandler;
    private String jiraUrl;
    private JiraClient jiraClient;
    private net.rcarz.jiraclient.JiraClient jiraSprintClient;


    public JarvisContext( final String jiraUrl )
    {
        authenticationHandler = new AnonymousAuthenticationHandler();
        this.jiraUrl = jiraUrl;
    }


    public JarvisContext( final String jiraUrl, AuthenticationHandler authenticationHandler )
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
        authenticationHandler = new BasicHttpAuthenticationHandler( username, password );
    }


    public JiraClient getJiraClient() throws JiraClientException
    {
        if ( jiraClient == null )
        {
            logger.debug(
                    String.format( "Creating JIRA client using [%s]...", authenticationHandler.getClass().getName() ) );
            jiraClient = new JiraClientImpl( jiraUrl, authenticationHandler );
        }

        return jiraClient;
    }


    public GreenHopperClient getJiraSprintClient()
    {
        if ( jiraSprintClient == null )
        {
            jiraSprintClient = new net.rcarz.jiraclient.JiraClient( jiraUrl, new CookieAuth( cookie.getValue() ) );
        }

        return new GreenHopperClient( jiraSprintClient );
    }


    public void destroy()
    {
        if ( jiraClient != null )
        {
            logger.debug( "Destoying JIRA client..." );
            try
            {
                jiraClient.close();
            }
            catch ( IOException e )
            {
                logger.error( e.getMessage(), e );
            }
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
