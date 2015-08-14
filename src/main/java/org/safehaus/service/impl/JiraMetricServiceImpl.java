package org.safehaus.service.impl;


import java.util.Collection;
import java.util.List;

import org.safehaus.analysis.JiraMetricIssue;
import org.safehaus.dao.Dao;
import org.safehaus.service.JiraMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class JiraMetricServiceImpl implements JiraMetricService
{
    private static final Logger log = LoggerFactory.getLogger( JiraMetricServiceImpl.class );

    @Autowired
    private Dao dao;


    public JiraMetricServiceImpl()
    {
    }


    @Override
    public void insertJiraMetricIssue( JiraMetricIssue jiraMetricIssue )
    {
        log.info( "Inserting JiraMetricIssue with id {}", jiraMetricIssue.getIssueId() );
        dao.insert( jiraMetricIssue );
    }


    @Override
    public JiraMetricIssue findJiraMetricIssueById( Long id )
    {
        log.info( "Finding JiraMetricIssue with id {}", id );
        return dao.findById( JiraMetricIssue.class, id );
    }


    @Override
    public JiraMetricIssue findJiraMetricIssueByPK( JiraMetricIssue jiraMetricIssue )
    {
        log.info( "Finding JiraMetricIssue with PK, id: {}", jiraMetricIssue.getIssueId() );
        return dao.findById( JiraMetricIssue.class, jiraMetricIssue );
    }


    @Override
    public List<JiraMetricIssue> findJiraMetricIssuesByStatus( String status )
    {
        String query = "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.status = " + status;

        log.info( "Finding JiraMetricIssues by status: {}", status );

        List<JiraMetricIssue> jiraMetricIssues = ( List<JiraMetricIssue> ) dao.findByQuery( query );
        return jiraMetricIssues;
    }


    @Override
    public List<JiraMetricIssue> findJiraMetricIssuesByIssueType( String issueType )
    {
        String query = "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.issueType = " + issueType;

        log.info( "Finding JiraMetricIssues by issueType: {}", issueType );

        List<JiraMetricIssue> jiraMetricIssues = ( List<JiraMetricIssue> ) dao.findByQuery( query );
        return jiraMetricIssues;
    }


    @Override
    public List<JiraMetricIssue> findJiraMetricIssuesByProjectKey( String projectKey )
    {
        String query =
                "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.projectKey = " + projectKey;

        log.info( "Finding JiraMetricIssues by projectKey: {}", projectKey );

        List<JiraMetricIssue> jiraMetricIssues = ( List<JiraMetricIssue> ) dao.findByQuery( query );
        return jiraMetricIssues;
    }


    @Override
    public List<JiraMetricIssue> findJiraMetricIssuesByReporterName( String reporterName )
    {
        String query =
                "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.reporterName = " + reporterName;

        log.info( "Finding JiraMetricIssues by reporterName: {}", reporterName );

        List<JiraMetricIssue> jiraMetricIssues = ( List<JiraMetricIssue> ) dao.findByQuery( query );
        return jiraMetricIssues;
    }


    @Override
    public List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName( String assigneeName )
    {
        String query =
                "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.assigneeName = " + assigneeName;

        log.info( "Finding JiraMetricIssues by assigneeName: {}", assigneeName );

        List<JiraMetricIssue> jiraMetricIssues = ( List<JiraMetricIssue> ) dao.findByQuery( query );
        return jiraMetricIssues;
    }


    @Override
    public List<JiraMetricIssue> findJiraMetricIssuesByResolution( String resolution )
    {
        String query =
                "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.resolution = " + resolution;

        log.info( "Finding JiraMetricIssues by resolution: {}", resolution );

        List<JiraMetricIssue> jiraMetricIssues = ( List<JiraMetricIssue> ) dao.findByQuery( query );
        return jiraMetricIssues;
    }


    @Override
    public void updateJiraMetricIssue( JiraMetricIssue jiraMetricIssue )
    {
        if ( jiraMetricIssue == null || jiraMetricIssue.getIssueId() == null )
        {
            throw new IllegalArgumentException( "Id should not be null to perform update." );
        }
        log.info( "Updating JiraMetricIssue, id: {}", jiraMetricIssue.getIssueId() );
        dao.merge( jiraMetricIssue );
    }


    @Override
    public void deleteJiraMetricIssue( JiraMetricIssue jiraMetricIssue )
    {
        if ( jiraMetricIssue == null || jiraMetricIssue.getIssueId() == null )
        {
            throw new IllegalArgumentException( "Id should not be null to perform delete." );
        }
        log.info( "Deleting JiraMetricIssue, id: {}", jiraMetricIssue.getIssueId() );
        dao.remove( jiraMetricIssue );
    }


    @Override
    public void batchInsert( final List<JiraMetricIssue> issues )
    {
        dao.batchInsert( issues );
    }


    public Dao getDao()
    {
        return dao;
    }


    public void setDao( final Dao dao )
    {
        this.dao = dao;
    }
}
