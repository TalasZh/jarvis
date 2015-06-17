package org.safehaus.service.impl;


import java.util.List;

import javax.jws.WebService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.model.JarvisIssue;
import org.safehaus.model.JarvisProject;
import org.safehaus.model.Views;
import org.safehaus.jira.JiraManager;
import org.safehaus.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;


@Service( "projectManager" )
@WebService( serviceName = "ProjectService", endpointInterface = "org.safehaus.service.ProjectService" )
public class ProjectServiceImpl implements ProjectService
{
    private static Logger logger = LoggerFactory.getLogger( ProjectServiceImpl.class );
    //    private static String CROWD_TOKEN_NAME = "crowd.token_key";


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
        //        setContext();
        JarvisProject result = jiraManager.getProject( projectId );
        result.setTeamMembers( jiraManager.getProjectMemebers( projectId ) );
        return result;
    }


    @Override
    public List<JarvisProject> getProjects()
    {
        //        setContext();
        try
        {
            return jiraManager.getProjects();
        }
        catch ( JiraClientException e )
        {
            logger.error( e.getMessage(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( e.getMessage() );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
    }


    @JsonView( Views.JarvisIssueShort.class )
    @Override
    public List<JarvisIssue> getIssues( final String projectId )
    {
        //        setContext();
        try
        {
            return jiraManager.getIssues( projectId );
        }
        catch ( JiraClientException e )
        {
            logger.error( e.getMessage(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( e.getMessage() );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
    }


    @JsonView( Views.JarvisIssueLong.class )
    @Override
    public JarvisIssue getIssue( final String issueId )
    {
        try
        {
            return jiraManager.getIssue( issueId );
        }
        catch ( JiraClientException e )
        {
            logger.error( e.getMessage(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( e.getMessage() );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
    }


    @Override
    public JarvisIssue createIssue( final JarvisIssue issue )
    {
        //        setContext();
        try
        {
            //            String token = getCookie( CROWD_TOKEN_NAME );
            return jiraManager.createIssue( issue );
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


    @Override
    public List<Transition> getTransitions( final String issueIdOrKey ) throws JiraClientException
    {
        return Lists.newArrayList( jiraManager.getTransitions( issueIdOrKey ) );
    }


    @Override
    public Status toTransition( final String issueIdOrKey, final String transitionId ) throws JiraClientException
    {
        return jiraManager.toTransition( issueIdOrKey, transitionId );
    }


    @Override
    public Status start( final String issueIdOrKey ) throws JiraClientException
    {
        return jiraManager.storyStart( issueIdOrKey );
    }


    @Override
    public Status resolve( final String issueIdOrKey ) throws JiraClientException
    {
        return jiraManager.storyResolve( issueIdOrKey );
    }


    @Override
    public Status requestApproval( final String issueIdOrKey ) throws JiraClientException
    {
        return jiraManager.storyRequestApproval( issueIdOrKey );
    }


    @Override
    public Status approve( final String issueIdOrKey ) throws JiraClientException
    {
        return jiraManager.storyApprove( issueIdOrKey );
    }


    @Override
    public Status reject( final String issueIdOrKey ) throws JiraClientException
    {
        return jiraManager.storyReject( issueIdOrKey );
    }
}
