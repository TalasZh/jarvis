package org.safehaus.service.impl;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.jws.WebService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.safehaus.model.Capture;
import org.safehaus.model.Session;
import org.safehaus.model.Views;
import org.safehaus.service.SessionManager;
import org.safehaus.service.SessionService;
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


    @Autowired
    public void setSessionManager( SessionManager sessionManager ) {this.sessionManager = sessionManager;}


    @Override
    @JsonView( Views.JarvisSessionLong.class )
    public Session getSession( final String sessionId )
    {
        return sessionManager.get( new Long( sessionId ) );
    }


    @Override
    @JsonView( Views.JarvisSessionShort.class )
    public List<Session> getSessions()
    {
        UserDetails userDetails = getUserDetails();

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
    public Session startSession( final String sessionId )
    {
        UserDetails userDetails = getUserDetails();

        if ( userDetails == null )
        {
            logger.error( "User details not found." );
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.CONFLICT );
            builder.entity( "The requested resource is conflicted." );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }

        try
        {
            return sessionManager.startSession( sessionId, userDetails.getUsername() );
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
    }


    @Override
    public Capture saveCapture( final String sessionId, final Capture capture )
    {
        Session session = sessionManager.getSession( sessionId );

        session.addCapture( capture );

        sessionManager.saveSession( session );

        return capture;
    }


    @Override
    public List<Capture> getCaptures( final String sessionId )
    {
        Session session = sessionManager.getSession( sessionId );
        return ImmutableList.copyOf( session.getCaptures() );
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


    private UserDetails getUserDetails()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ( !( auth instanceof AnonymousAuthenticationToken ) )
        {
            UserDetails userDetails = ( UserDetails ) auth.getPrincipal();
            logger.debug( userDetails.getUsername() );
            return userDetails;
        }
        else
        {
            return null;
        }
    }
}
