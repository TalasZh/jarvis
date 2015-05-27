package org.safehaus.service.impl;


import java.util.List;

import javax.jws.WebService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.safehaus.jira.api.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisProject;
import org.safehaus.model.Views;
import org.safehaus.service.JiraManager;
import org.safehaus.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.fasterxml.jackson.annotation.JsonView;


@Service( "projectManager" )
@WebService( serviceName = "ProjectService", endpointInterface = "org.safehaus.service.ProjectService" )
public class ProjectServiceImpl implements ProjectService
{
    private static Logger logger = LoggerFactory.getLogger( ProjectServiceImpl.class );
    private static String CROWD_TOKEN_NAME = "crowd.token_key";


    private JiraManager jiraManager;


    @Autowired
    public void setJiraManager( final JiraManager jiraManager )
    {
        this.jiraManager = jiraManager;
    }


    //    @Autowired
    //    public void setProjectDao( final ProjectDao projectDao )
    //    {
    //        this.dao = projectDao;
    //        this.projectDao = projectDao;
    //    }


    @Override
    public JarvisProject getProject( final String projectId ) throws JiraClientException
    {
        JarvisProject result = jiraManager.getProject( projectId );
        result.setTeamMembers( jiraManager.getProjectMemebers( projectId ) );
        return result;
    }


    @Override
    public List<JarvisProject> getProjects()
    {
        return jiraManager.getProjects();
    }


    @JsonView( Views.JarvisIssueShort.class )
    @Override
    public List<JarvisIssue> getIssues( final String projectId )
    {
        return jiraManager.getIssues( projectId );
    }


    @JsonView( Views.JarvisIssueLong.class )
    @Override
    public JarvisIssue getIssue( final String issueId )
    {
        return jiraManager.getIssue( issueId );
    }


    @Override
    public JarvisIssue createIssue( final JarvisIssue issue )
    {
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if ( !( auth instanceof AnonymousAuthenticationToken ) )
            {
                UserDetails userDetails = ( UserDetails ) auth.getPrincipal();
                logger.debug( userDetails.getUsername() );
            }
            String token = getCookie( CROWD_TOKEN_NAME );
            return jiraManager.createIssue( issue, token );
        }
        catch ( JiraClientException jce )
        {
            logger.error( jce.getMessage() );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( "The requested resource is conflicted." );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
    }


    private String getCookie( final String cookieName )
    {
        // Here We are getting cookies from HttpServletRequest
        Message message = PhaseInterceptorChain.getCurrentMessage();
        HttpServletRequest request = ( HttpServletRequest ) message.get( AbstractHTTPDestination.HTTP_REQUEST );
        Cookie[] cookies = request.getCookies();
        String result = null;
        if ( cookies != null )
        {
            logger.debug( "Cookies:" );
            for ( Cookie cookie : cookies )
            {
                logger.debug( String.format( "\t%s=%s", cookie.getName(), cookie.getValue() ) );
                if ( cookie.getName().equals( cookieName ) )
                {
                    result = cookie.getValue();
                }
            }
        }
        return result;
    }
}
