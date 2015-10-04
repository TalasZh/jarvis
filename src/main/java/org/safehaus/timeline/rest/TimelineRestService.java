package org.safehaus.timeline.rest;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.safehaus.model.Views;
import org.safehaus.timeline.model.StoryTimeline;
import org.safehaus.timeline.model.StructuredProject;
import org.safehaus.timeline.model.UserInfo;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * Created by talas on 9/27/15.
 */
@Path( "/timeline" )
@WebService
public interface TimelineRestService
{
    @GET
    @Path( "project/{projectKey}" )
    @JsonView( Views.TimelineLong.class )
    public StructuredProject getProject( @PathParam( "projectKey" ) String projectKey );


    @GET
    @Path( "project" )
    @JsonView( Views.TimelineShort.class )
    public List<StructuredProject> getProjects();


    @GET
    @Path( "user/{username}" )
    @JsonView( Views.TimelineShort.class )
    public UserInfo getUserInfo( @PathParam( "username" ) String username );


    @GET
    @Path( "story/{storyKey}" )
    @JsonView( Views.CompleteView.class )
    public StoryTimeline getStoryTimeline( @PathParam( "storyKey" ) String storyKey,
                                           @QueryParam( "from" ) String fromDate, @QueryParam( "to" ) String toDate );
}
