package org.safehaus.service;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.safehaus.model.Capture;
import org.safehaus.model.Session;
import org.safehaus.model.User;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * Web Service interface so hierarchy of Generic Manager isn't carried through.
 */
@WebService
@Path( "/sessions" )
public interface SessionService
{
    /**
     * Retrieves a session by userId.  An exception is thrown if session not found
     *
     * @param sessionId the identifier for the user
     *
     * @return Session
     */
    @GET
    @Path( "{id}" )
    @JsonView( Views.JarvisSessionLong.class )
    Session getSession( @PathParam( "id" ) String sessionId );

    /**
     * Retrieves a list of all user's sessions.
     *
     * @return List
     */
    @GET
    @JsonView( Views.JarvisSessionShort.class )
    List<Session> getSessions();

    /**
     * start user's session
     *
     * @return List
     */
    @GET
    @Path( "{sessionId}/start" )
    Session startSession( @PathParam( "sessionId" ) String sessionId );

    /**
     * Store user's capture
     *
     * @return capture capture object
     */
    @POST
    @Path( "{sessionId}/capture" )
    Capture saveCapture( @PathParam( "sessionId" ) String sessionId, Capture capture );

    @GET
    @Path( "{sessionId}/capture" )
    List<Capture> getCaptures( @PathParam( "sessionId" ) String sessionId );
}
