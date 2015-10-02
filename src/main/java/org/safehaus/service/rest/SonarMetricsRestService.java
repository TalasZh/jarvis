package org.safehaus.service.rest;


import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.safehaus.dao.entities.sonar.SonarMetricIssue;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * Created by ermek on 10/1/15.
 */
@WebService
@Path( "sonar/metrics" )
public interface SonarMetricsRestService
{
    @GET
    @Path( "{id}" )
    @JsonView( Views.CompleteView.class )
    public SonarMetricIssue findSonarMetricIssueByProjectId( @PathParam( "id" ) String id );

}
