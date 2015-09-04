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
    public List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName( String assigneeName )
    {
        String query =
                "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.assigneeName = " + assigneeName;

        log.info( "Finding JiraMetricIssues by assigneeName: {}", assigneeName );

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
