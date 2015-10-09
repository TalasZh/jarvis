package org.safehaus.service.impl;


import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.stash.StashMetricIssue;
import org.safehaus.dao.entities.stash.StashUser;
import org.safehaus.service.api.StashMetricService;
import org.safehaus.service.rest.StashMetricsRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


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
    public StashMetricIssue findStashMetricIssueById( String id )
    {
        log.info( "Finding StashMetricIssue by id: {}", id );

        String parameter = "contentId";
        String query = String.format( "SELECT c FROM %s c WHERE c.stashMetricPK.contentId = :%s",
                StashMetricIssue.class.getSimpleName(), parameter );
        Map<String, Object> params = Maps.newHashMap();
        params.put( parameter, id );

        List<StashMetricIssue> results = dao.findByQueryWithLimit( StashMetricIssue.class, query, params, 1 );
        if ( results.size() > 0 )
        {
            return results.get( 0 );
        }
        return null;
    }


    @Override
    public List<StashMetricIssue> findStashMetricIssuesByProjectName( String projectName )
    {
        String query =
                "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.projectName = " + projectName;

        log.info( "Finding StashMetricIssue by projectName : {}", projectName );
        List<StashMetricIssue> stashMetricIssues = ( List<StashMetricIssue> ) dao.findByQuery( query );
        return stashMetricIssues;
    }


    @Override
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
    public List<StashMetricIssue> getStashMetricIssuesByAuthor( final String authorId )
    {
        String query = "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.author = " + authorId;

        log.info( "Finding StashMetricIssue by projectName : {}", authorId );
        List<StashMetricIssue> stashMetricIssues = ( List<StashMetricIssue> ) dao.findByQuery( query );
        return stashMetricIssues;
    }


    @Override
    public List<StashMetricIssue> getStashMetricIssuesByUsername( final String username, int limit )
    {
        String parameter = "author";
        String authorQuery =
                String.format( "SELECT u FROM %s u WHERE u.slug = :%s", StashUser.class.getSimpleName(), parameter );
        Map<String, Object> authorParameters = Maps.newHashMap();
        authorParameters.put( parameter, username );

        List<StashUser> stashUser = dao.findByQuery( StashUser.class, authorQuery, authorParameters );
        List<StashMetricIssue> stashMetricIssues = Lists.newArrayList();
        if ( stashUser.size() > 0 )
        {
            String query =
                    String.format( "Select j from %s j where j.author = :%s", StashMetricIssue.class.getSimpleName(),
                            parameter );
            Map<String, Object> parameters = Maps.newHashMap();
            parameters.put( parameter, stashUser.get( 0 ).getId() );

            stashMetricIssues = dao.findByQueryWithLimit( StashMetricIssue.class, query, parameters, limit );
        }
        return stashMetricIssues;
    }


    @Override
    public List<StashMetricIssue> getStashMetricIssues()
    {
        return dao.getAll( StashMetricIssue.class );
    }


    @Override
    public List<StashMetricIssue> getStashMetricIssues( final int limit, final int startPosition )
    {
        return dao.getAll( StashMetricIssue.class, limit, startPosition );
    }


    @Override
    public List<StashMetricIssue> getStashMetricIssuesByAuthorTimestamp( final String timestamp )
    {
        String query =
                "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.authorTimestamp = " + timestamp;

        log.info( "Finding StashMetricIssue by projectName : {}", timestamp );
        List<StashMetricIssue> stashMetricIssues = ( List<StashMetricIssue> ) dao.findByQuery( query );
        return stashMetricIssues;
    }


    @Override
    public List<StashMetricIssue> getStashMetricIssueForTimeFrame( final String fromDate, final String toDate )
    {
        String query = "Select j from " + StashMetricIssue.class.getSimpleName()
                + " j where (j.stashMetricPK.authorTs > :fromDate) and (j.stashMetricPK.authorTs < :toDate)";

        log.info( "Get list of StashMetricIssue by time period : {} {}", fromDate, toDate );

        List<StashMetricIssue> issues =
                ( List<StashMetricIssue> ) dao.findByQuery( query, "fromDate", fromDate, "toDate", toDate );
        return issues;
    }


    @Override
    public List<StashMetricIssue> getStashMetricIssuesByAuthorForTimeFrame( final String author, final String fromDate,
                                                                            final String toDate )
    {
        String authorQuery =
                String.format( "SELECT u FROM %s u WHERE u.name = :author", StashUser.class.getSimpleName() );
        Map<String, Object> authorParameters = Maps.newHashMap();
        authorParameters.put( "author", author );

        List<StashUser> stashUser = dao.findByQuery( StashUser.class, authorQuery, authorParameters );
        List<StashMetricIssue> stashMetricIssues = Lists.newArrayList();
        if ( stashUser.size() > 0 )
        {
            String query = String.format(
                    "Select j from %s j where j.stashMetricPK.authorTs > :fromDate and j.stashMetricPK.authorTs < "
                            + ":toDate and j.author = :author", StashMetricIssue.class.getSimpleName() );
            Map<String, Object> parameters = Maps.newHashMap();
            parameters.put( "fromDate", fromDate );
            parameters.put( "toDate", toDate );
            //            parameters.put( "author", author );
            parameters.put( "author", stashUser.iterator().next().getId() );

            log.info( "Get list of StashMetricIssue by time period : {} {}", fromDate, toDate );

            stashMetricIssues = dao.findByQuery( StashMetricIssue.class, query, parameters );
        }
        return stashMetricIssues;
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
