package org.safehaus.service.rest;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.safehaus.dao.entities.stash.StashMetricIssue;


/**
 * Created by talas on 9/8/15.
 */
@WebService
@Path( "/metrics" )
public interface StashMetricsRestService
{
    @GET
    @Path( "{id}" )
    public StashMetricIssue findStashMetricIssueById( @PathParam( "id" ) String id );

    @GET
    @Path( "projects/{projectKey}" )
    public List<StashMetricIssue> getStashMetricsByProjectKey( @PathParam( "projectKey" ) String projectKey );

    @GET
    @Path( "author/{authorId}" )
    public List<StashMetricIssue> getStashMetricIssuesByAuthor( @PathParam( "authorId" ) String authorId );

    @GET
    @Path( "timestamp/{timestamp}" )
    public List<StashMetricIssue> getStashMetricIssuesByAuthorTimestamp( @PathParam( "timestamp" ) String timestamp );
}
