package org.safehaus.service.rest;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.safehaus.analysis.JiraIssueChangelog;


/**
 * Created by ermek on 9/21/15.
 */
@WebService
@Path( "/jira/changelog" )
public interface JiraChangelogRestService
{
    @GET
    @Path( "filter" )
    public List<JiraIssueChangelog> findIssueChangelogByFilter( @QueryParam( "author" ) String author,
                                                                @QueryParam( "field" ) String field,
                                                                @QueryParam( "type" ) String type );
}