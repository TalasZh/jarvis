package org.safehaus.service;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.safehaus.jira.api.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisProject;
import org.safehaus.model.ProjectStatus;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;


@WebService
public interface ProjectService
{
    @GET
    @Path( "projects" )
    @JsonView( Views.JarvisProjectShort.class )
    List<JarvisProject> getProjects();

    @GET
    @Path( "projects/{projectId}" )
    @JsonView( Views.JarvisProjectLong.class )
    JarvisProject getProject( @PathParam( "projectId" ) String projectId ) throws JiraClientException;

    @GET
    @Path( "projects/{projectId}/issues" )
    @JsonView( Views.JarvisIssueShort.class )
    List<JarvisIssue> getIssues( @PathParam( "projectId" ) String projectId );

    @GET
    @Path( "issues/{issueId}" )
    @JsonView( Views.JarvisIssueLong.class )
    JarvisIssue getIssue( @PathParam( "issueId" ) String issueId );

    //    @GET
    //    @Path( "status/{projectId}" )
    //    ProjectStatus getProjectStatus( @PathParam( "projectId" ) String sessionId );
    //
    //    @GET
    //    @Path( "status/me/{projectId}" )
    //    String getSessionUserStage( @PathParam( "projectId" ) String sessionId );

    //    @POST
    //    @Path( "save/{sessionId}" )
    //    JarvisProject saveProject( JarvisProject project );
}
