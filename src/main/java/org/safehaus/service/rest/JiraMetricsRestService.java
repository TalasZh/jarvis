package org.safehaus.service.rest;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.jira.JiraProject;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * Created by ermek on 9/16/15.
 */
@Path( "/jira/metrics" )
@WebService
public interface JiraMetricsRestService
{
    @GET
    @Path( "{id}" )
    @JsonView( Views.CompleteView.class )
    public JiraMetricIssue findJiraMetricIssueById( @PathParam( "id" ) String id );

    @GET
    @Path( "assignee/{assigneeName}" )
    @JsonView( Views.CompleteView.class )
    public List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName( @PathParam( "assigneeName" ) String assigneeName );

    @GET
    @Path( "key/{id}" )
    @JsonView( Views.CompleteView.class )
    public List<JiraMetricIssue> findJiraMetricIssueByKeyId( @PathParam( "id" ) String keyId );

    @GET
    @Path( "project/{key}" )
    @JsonView( Views.CompleteView.class )
    public List<JiraProject> getProjectByKey( @PathParam( "key" ) String key );

    @GET
    @Path( "project/{projectId}/id" )
    @JsonView( Views.CompleteView.class )
    public List<JiraProject> getProjectById( @PathParam( "projectId" ) Long projectId );
}