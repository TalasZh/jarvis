package org.safehaus.service.impl;


import java.util.List;

import javax.jws.WebService;

import org.codehaus.jackson.map.annotate.JsonView;
import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.stash.StashMetricIssue;
import org.safehaus.model.Views;
import org.safehaus.service.api.StashMetricService;
import org.safehaus.service.rest.StashMetricsRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service( "stashMetricsManager" )
@WebService( serviceName = "StashMetricServiceImpl",
        endpointInterface = "org.safehaus.service.rest.StashMetricsRestService" )
public class StashMetricServiceImpl implements StashMetricService, StashMetricsRestService
{
    private static final Logger log = LoggerFactory.getLogger( StashMetricServiceImpl.class );

    @Autowired
    private Dao dao;


    @Override
    public void insertStashMetricIssue( StashMetricIssue stashMetricIssue )
    {
        log.info( "Inserting new StashMetricIssue" );
        dao.insert( stashMetricIssue );
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public StashMetricIssue findStashMetricIssueById( String id )
    {
        log.info( "Finding StashMetricIssue by id: {}", id );
        return dao.findById( StashMetricIssue.class, id );
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> findStashMetricIssuesByProjectName( String projectName )
    {
        String query =
                "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.projectName = " + projectName;

        log.info( "Finding StashMetricIssue by projectName : {}", projectName );
        List<StashMetricIssue> stashMetricIssues = ( List<StashMetricIssue> ) dao.findByQuery( query );
        return stashMetricIssues;
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricsByProjectKey( String projectKey )
    {
        String query =
                "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.projectKey = :projectKey";

        log.info( "Finding StashMetricIssue by projectName : {}", projectKey );
        List<StashMetricIssue> stashMetricIssues =
                ( List<StashMetricIssue> ) dao.findByQuery( query, "projectKey", projectKey );
        return stashMetricIssues;
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricIssuesByAuthor( final String authorId )
    {
        String query = "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.author.id = " + authorId;

        log.info( "Finding StashMetricIssue by projectName : {}", authorId );
        List<StashMetricIssue> stashMetricIssues = ( List<StashMetricIssue> ) dao.findByQuery( query );
        return stashMetricIssues;
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricIssuesByAuthorTimestamp( final String timestamp )
    {
        String query =
                "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.authorTimestamp = " + timestamp;

        log.info( "Finding StashMetricIssue by projectName : {}", timestamp );
        List<StashMetricIssue> stashMetricIssues = ( List<StashMetricIssue> ) dao.findByQuery( query );
        return stashMetricIssues;
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<StashMetricIssue> getStashMetricIssueByTimePeriod( final String fromDate, final String toDate )
    {
        String query = "Select j from " + StashMetricIssue.class.getSimpleName()
                + " j where (j.stashMetricPK.authorTs > :fromDate) and (j.stashMetricPK.authorTs < :toDate)";

        log.info( "Get list of StashMetricIssue by time period : {} {}", fromDate, toDate );

        List<StashMetricIssue> issues =
                ( List<StashMetricIssue> ) dao.findByQuery( query, "fromDate", fromDate, "toDate", toDate );
        return issues;
    }


    @Override
    public void updateStashMetricIssue( StashMetricIssue stashMetricIssue )
    {
        if ( stashMetricIssue == null )
        {
            throw new IllegalArgumentException( "Entity or Entity.id should not be null to perform update." );
        }

        log.info( "Updating StashMetricIssue with id {}", stashMetricIssue.getId() );
        dao.merge( stashMetricIssue );
    }


    @Override
    public void deleteStashMetricIssue( StashMetricIssue stashMetricIssue )
    {
        if ( stashMetricIssue == null )
        {
            throw new IllegalArgumentException( "Entity or Entity.id should not be null to perform delete." );
        }

        log.info( "Removing StashMetricIssue with id {}", stashMetricIssue.getId() );
        dao.remove( stashMetricIssue );
    }


    @Override
    public int batchInsert( final List<StashMetricIssue> issues )
    {
        return dao.batchInsert( issues );
    }
}
