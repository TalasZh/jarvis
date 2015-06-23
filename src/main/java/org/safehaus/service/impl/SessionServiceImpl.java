package org.safehaus.service.impl;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.safehaus.confluence.helpers.ApiHelper;
import org.safehaus.confluence.helpers.JsonHelper;
import org.safehaus.confluence.models.Content;
import org.safehaus.confluence.models.Results;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.Capture;
import org.safehaus.jira.model.JarvisIssue;
import org.safehaus.jira.model.JarvisLink;
import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;
import org.safehaus.model.Views;
import org.safehaus.jira.JiraManager;
import org.safehaus.service.SessionManager;
import org.safehaus.service.SessionService;
import org.safehaus.util.JarvisContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.ImmutableList;


/**
 * Created by tzhamakeev on 5/27/15.
 */
@Service( "sessionServiceImpl" )
@WebService( serviceName = "SessionService", endpointInterface = "org.safehaus.service.SessionService" )
public class SessionServiceImpl implements SessionService
{
    private static Logger logger = LoggerFactory.getLogger( SessionServiceImpl.class );
    private static String CROWD_TOKEN_NAME = "crowd.token_key";


    private SessionManager sessionManager;
    private JiraManager jiraManager;
    //    private PhaseManager phaseManager;


    @Autowired
    public void setSessionManager( SessionManager sessionManager ) {this.sessionManager = sessionManager;}


    @Autowired
    public void setJiraManager( final JiraManager jiraManager )
    {
        this.jiraManager = jiraManager;
    }

    //
    //    @Autowired
    //    public void setPhaseManager( final PhaseManager phaseManager )
    //    {
    //        this.phaseManager = phaseManager;
    //    }


    @Override
    @JsonView( Views.JarvisSessionLong.class )
    public Session getSession( final String sessionId )
    {
        try
        {
            return sessionManager.getSession( sessionId );
        }
        catch ( SessionNotFoundException e )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
    }


    @Override
    @JsonView( Views.JarvisSessionShort.class )
    public List<Session> getSessions()
    {
        UserDetails userDetails = JarvisContextHolder.getContext().getUserDetails();

        if ( userDetails == null )
        {
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.UNAUTHORIZED );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }

        return sessionManager.getSessionsByUsername( userDetails.getUsername() );
    }


    @Override
    public Session startSession( final String issueId )
    {
        UserDetails userDetails = JarvisContextHolder.getContext().getUserDetails();

        if ( userDetails == null )
        {
            logger.error( "User details not found." );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( "The requested resource is conflicted." );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }

        //        for ( Iterator i = userDetails.getAuthorities().iterator(); i.hasNext(); )
        //        {
        //            Object o = i.next();
        //            logger.debug( "========> " + o.toString() );
        //        }

        try
        {
            return sessionManager.startSession( issueId, userDetails.getUsername() );
        }
        catch ( Exception | JiraClientException e )
        {
            logger.error( e.toString(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( e.toString() );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
    }


    @Override
    @JsonView( Views.JarvisSessionShort.class )
    public Session pauseSession( final String sessionId )
    {
        try
        {
            return sessionManager.pauseSession( sessionId );
        }
        catch ( Exception e )
        {
            logger.error( e.toString() );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( "The requested resource is conflicted." );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
        catch ( SessionNotFoundException e )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
    }


    @Override
    @JsonView( Views.JarvisSessionShort.class )
    public Session closeSession( final String sessionId )
    {
        try
        {
            return sessionManager.closeSession( sessionId );
        }
        catch ( Exception e )
        {
            logger.error( e.toString(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( "The requested resource is conflicted." );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
        catch ( SessionNotFoundException e )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
    }


    @Override
    public Capture saveCapture( final String sessionId, final Capture capture )
    {
        try
        {
            return sessionManager.addCapture( sessionId, capture );
        }
        catch ( Exception e )
        {
            logger.error( e.toString() );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( "The requested resource is conflicted." );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
        catch ( SessionNotFoundException e )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
    }


    @Override
    public Capture updateCapture( final String sessionId, final String captureId, final Capture capture )
    {
        try
        {
            return sessionManager.updateCapture( sessionId, captureId, capture );
        }
        catch ( SessionNotFoundException e )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
    }


    @Override
    public Response deleteCapture( String sessionId, String captureId )
    {
        try
        {
            sessionManager.deleteCapture( sessionId, captureId );
            return Response.ok().build();
        }
        catch ( SessionNotFoundException e )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
    }


    @Override
    public List<Capture> getCaptures( final String sessionId )
    {
        Session session = null;
        try
        {
            session = sessionManager.getSession( sessionId );
        }
        catch ( SessionNotFoundException e )
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
        return ImmutableList.copyOf( session.getCaptures() );
    }


    @Override
    public Response resolveIssue( final String issueId )
    {
        try
        {
            List<JarvisIssue> blockedChains = new ArrayList();
            jiraManager.buildBlocksChain( issueId, blockedChains );

            for ( JarvisIssue blockedIssue : blockedChains )
            {
                logger.debug( String.format( "%s %s", blockedIssue.getKey(), blockedIssue.getSummary() ) );
            }

            jiraManager.resolveIssue( issueId );


            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.OK );
            return builder.build();
        }
        catch ( Exception | JiraClientException e )
        {
            logger.error( e.toString(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( e.toString() );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }
    }


    private Content generateSubPage( String credentails, int ancestorId, String issueKey )
            throws IOException, URISyntaxException
    {
        JarvisIssue jarvisIssue = null;
        try
        {
            jarvisIssue = jiraManager.getIssue( issueKey );
        }
        catch ( JiraClientException e )
        {
            logger.error( e.toString(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( e.toString() );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }

        String title = String.format( "%s. %s", jarvisIssue.getKey(), jarvisIssue.getSummary() );

        String spaceKey = jarvisIssue.getProjectKey();
        String result = ApiHelper.queryContent( credentails, String.format( "spaceKey=%s&title=%s", spaceKey, title ) );

        Results contentResults = JsonHelper.parseResults( result );
        List<Content> contents = contentResults.fields.getArrayAsContents();

        Content storyContent = contents.size() == 1 ? contents.get( 0 ) : null;

        if ( storyContent == null )
        {
            storyContent = new Content( ApiHelper.createSubPage( credentails, title, spaceKey, "", ancestorId ) );
        }

        int version = storyContent.fields.getVersion();

        StringBuilder sb = new StringBuilder();
        if ( "Research".equals( jarvisIssue.getType().getName() ) )
        {
            Session session = null;
            try
            {
                session = sessionManager.getSession( jarvisIssue.getId().toString() );
                sb.append( String.format( "<h3>Captured information</h3>" ) );
                sb.append( "<table><tr>" + "<td>Created</td>" + "<td>Comment</td>" + "<td>Link</td>"
                        + "<td>Anchor text</td>" + "</tr>" );

                for ( Capture capture : session.getCaptures() )
                {
                    sb.append(
                            String.format( "<tr><td>%s</td><td>%s</td><td><a href='%s'>Link</a></td><td>%s</td></tr>",
                                    capture.getCreated(), capture.getComment(), capture.getUrl(),
                                    capture.getAnchorText() ) );
                }
                sb.append( "</table>" );
            }
            catch ( SessionNotFoundException e )
            {
                logger.debug( e.toString() );
            }
        }
        else
        {
            sb.append( "<h4>Links:</h4><ul>" );

            for ( JarvisLink jarvisLink : jarvisIssue.getLinks() )
            {
                if ( jarvisLink.getLinkType().equals( "Blocks" ) && jarvisLink.getLinkDirection().equals( "INBOUND" ) )
                {
                    Content subPage = generateSubPage( credentails, storyContent.fields.getId(), jarvisLink.getKey() );
                    sb.append( String.format( "<li><a href='%s'>%s</a></li>", subPage.fields.getWebui(),
                            subPage.fields.getTitle() ) );
                }
            }
            sb.append( "</ul>" );
        }

        result = StringEscapeUtils.escapeJson( sb.toString() );

        ApiHelper.updateSubPage( credentails, storyContent.fields.getId(), title, spaceKey, version + 1, false, result,
                ancestorId );

        result = ApiHelper.queryContent( credentails, String.format( "spaceKey=%s&title=%s", spaceKey, title ) );
        contentResults = JsonHelper.parseResults( result );
        contents = contentResults.fields.getArrayAsContents();

        storyContent = contents.size() == 1 ? contents.get( 0 ) : null;

        if ( storyContent != null )
        {
            //TODO: find main page and add link to story
            //            result = ApiHelper.queryContent( credentails, String.format( "spaceKey=%s&type=page",
            // spaceKey ) );
        }

        return storyContent;
    }


    @Override
    public Response generate( final String issueId )
    {
        try
        {
            //            setSecurityContext();
            final String credentails = String.format( "%s=%s", CROWD_TOKEN_NAME, getCookie( CROWD_TOKEN_NAME ) );

            //            String remoteAddress = getRemoteAddress();
            JarvisIssue jarvisIssue = jiraManager.getIssue( issueId );


            String title = String.format( "%s. %s", jarvisIssue.getKey(), jarvisIssue.getSummary() );

            String spaceKey = jarvisIssue.getProjectKey();
            String result =
                    ApiHelper.queryContent( credentails, String.format( "spaceKey=%s&title=%s", spaceKey, title ) );

            Results contentResults = JsonHelper.parseResults( result );
            List<Content> contents = contentResults.fields.getArrayAsContents();

            Content storyContent = contents.size() == 1 ? contents.get( 0 ) : null;

            if ( storyContent == null )
            {
                storyContent = new Content( ApiHelper.createPage( credentails, title, spaceKey, "" ) );
            }

            int version = storyContent.fields.getVersion();

            StringBuilder sb = new StringBuilder( "<h4>Links:</h4><ul>" );
            for ( JarvisLink jarvisLink : jarvisIssue.getLinks() )
            {
                if ( jarvisLink.getLinkType().equals( "Blocks" ) && jarvisLink.getLinkDirection().equals( "INBOUND" ) )
                {
                    Content subPage = generateSubPage( credentails, storyContent.fields.getId(), jarvisLink.getKey() );
                    sb.append( String.format( "<li><a href='%s'>%s</a></li>", subPage.fields.getWebui(),
                            subPage.fields.getTitle() ) );
                }
            }
            sb.append( "</ul>" );

            ApiHelper.updatePage( credentails, storyContent.fields.getId(), title, spaceKey, version + 1, false,
                    sb.toString() );

            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.OK );
            return builder.build();
        }
        catch ( Exception | JiraClientException e )
        {
            logger.error( e.toString(), e );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( e.toString() );
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

    //
    //    private String getRemoteAddress()
    //    {
    //        // Here We are getting cookies from HttpServletRequest
    //        Message message = PhaseInterceptorChain.getCurrentMessage();
    //        HttpServletRequest request = ( HttpServletRequest ) message.get( AbstractHTTPDestination.HTTP_REQUEST );
    //
    //
    //        String result = request.getRemoteAddr();
    //        logger.debug( String.format( "Remote client address: %s", result ) );
    //        return result;
    //    }
}
