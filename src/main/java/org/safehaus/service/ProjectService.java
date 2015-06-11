package org.safehaus.service;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisProject;
import org.safehaus.model.Views;

import com.atlassian.jira.rest.client.api.domain.Transition;
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

    @POST
    @Path( "issues" )
    @Consumes( MediaType.APPLICATION_JSON )
    JarvisIssue createIssue( JarvisIssue issue );

    @GET
    @Path( "transitions/{issueIdOrKey}" )
    @Consumes( MediaType.APPLICATION_JSON )
    @JsonView( Views.CompleteView.class )
    List<Transition> getTransition( @PathParam( "issueIdOrKey" ) String issueIdOrKey ) throws JiraClientException;
}
