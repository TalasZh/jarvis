package org.safehaus.service.impl;


import java.util.List;

import javax.jws.WebService;

import org.safehaus.analysis.JiraMetricIssue;
import org.safehaus.dao.Dao;
import org.safehaus.model.Views;
import org.safehaus.service.JiraMetricService;
import org.safehaus.service.rest.JiraMetricsRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonView;


@Service( "jiraMetricsManager" )
@WebService( serviceName = "JiraMetricServiceImpl",
        endpointInterface = "org.safehaus.service.rest.JiraMetricsRestService" )
public class JiraMetricServiceImpl implements JiraMetricService, JiraMetricsRestService
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
    @JsonView( Views.CompleteView.class )
    public JiraMetricIssue findJiraMetricIssueById( final String id )
    {
        log.info( "Finding JiraMetricIssue with id {}", id );
        JiraMetricIssue result = new JiraMetricIssue();
        JiraMetricIssue tmp = dao.findById( JiraMetricIssue.class, Long.valueOf( id ) );
        if ( tmp != null )
        {
            result = tmp;
        }
        return result;
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName( final String assigneeName )
    {
        String query =
                "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.assigneeName =:assignee_name"
                        + assigneeName;

        log.info( "Finding JiraMetricIssues by assigneeName: {}", assigneeName );

        List<JiraMetricIssue> jiraMetricIssues =
                ( List<JiraMetricIssue> ) dao.findByQuery( query, "assignee_name", assigneeName );
        return jiraMetricIssues;
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<JiraMetricIssue> findJiraMetricIssueByKeyId( final String keyId )
    {
        String query = "Select j from " + JiraMetricIssue.class.getSimpleName() + " j where j.issueKey =:issue_key";
        log.info( "Finding JiraMetricIssue by key id: {}", keyId );

        List<JiraMetricIssue> jiraMetricIssues = ( List<JiraMetricIssue> ) dao.findByQuery( query, "issue_key", keyId );
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
