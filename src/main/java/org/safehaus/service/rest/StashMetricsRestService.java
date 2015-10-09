package org.safehaus.service.rest;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.map.annotate.JsonView;
import org.safehaus.dao.entities.stash.StashMetricIssue;
import org.safehaus.model.Views;


/**
 * Created by talas on 9/8/15.
 */
@WebService
@Path( "/stash" )
public interface StashMetricsRestService
{
    @GET
    @Path( "{id}" )
    @JsonView( Views.CompleteView.class )
    public StashMetricIssue findStashMetricIssueById( @PathParam( "id" ) String id );

    @GET
    @Path( "projects/{projectKey}" )
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricsByProjectKey( @PathParam( "projectKey" ) String projectKey );

    @GET
    @Path( "author/{authorId}" )
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricIssuesByAuthor( @PathParam( "authorId" ) String authorId );

    @GET
    @Path( "timestamp/{timestamp}" )
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricIssuesByAuthorTimestamp( @PathParam( "timestamp" ) String timestamp );


    @GET
    @Path( "commits" )
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricIssueForTimeFrame( @QueryParam( "from" ) String fromDate,
                                                                   @QueryParam( "to" ) String toDate );

    @GET
    @Path( "commits/{author}" )
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricIssuesByAuthorForTimeFrame( @PathParam( "author" ) String author,
                                                                            @QueryParam( "from" ) String fromDate,
                                                                            @QueryParam( "to" ) String toDate );
}
