package org.safehaus.service.rest;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.safehaus.analysis.JiraMetricIssue;


/**
 * Created by ermek on 9/16/15.
 */
@Path( "/jira/metrics" )
@WebService
public interface JiraMetricsRestService
{
    @GET
    @Path( "{id}" )
    public JiraMetricIssue findJiraMetricIssueById(@PathParam( "id" ) String id );

    @GET
    @Path( "assignee/{assigneeName}" )
    public List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName(@PathParam( "assigneeName" ) String assigneeName );

    @GET
    @Path( "key/{id}" )
    public List<JiraMetricIssue> findJiraMetricIssueByKeyId(@PathParam( "id" ) String keyId);
}


