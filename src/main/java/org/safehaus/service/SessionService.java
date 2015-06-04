package org.safehaus.service;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.safehaus.model.Capture;
import org.safehaus.model.Session;


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
    Session getSession( @PathParam( "id" ) String sessionId );

    /**
     * Retrieves a list of all user's sessions.
     *
     * @return List
     */
    @GET
    List<Session> getSessions();

    /**
     * Starts user's session
     *
     * @return Session
     */
    @PUT
    @Path( "{issueId}/start" )
    Session startSession( @PathParam( "issueId" ) String issueId );

    /**
     * Pauses user's session
     *
     * @return Session
     */
    @PUT
    @Path( "{sessionId}/pause" )
    Session pauseSession( @PathParam( "sessionId" ) String sessionId );

    /**
     * Closes user's session
     *
     * @return Session
     */
    @PUT
    @Path( "{sessionId}/stop" )
    Session closeSession( @PathParam( "sessionId" ) String sessionId );

    /**
     * Resolves research phase
     *
     * @return JarvisIssue
     */
    @PUT
    @Path( "{issueId}/resolve" )
    Response resolveIssue( @PathParam( "issueId" ) String issueId );

    /**
     * Generate confluence pages tree
     *
     * @return Response
     */
    @POST
    @Path( "{issueId}/generate" )
    Response generate( @PathParam( "issueId" ) String issueId );

    /**
     * Creates user's capture
     *
     * @return capture capture object
     */
    @PUT
    @Path( "{sessionId}/capture/{captureId}" )
    Capture updateCapture( @PathParam( "sessionId" ) String sessionId, @PathParam( "captureId" ) String captureId,
                           Capture capture );

    /**
     * Updates user's capture
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
