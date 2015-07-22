package org.safehaus.service;


import java.util.List;
import org.safehaus.analysis.JiraMetricIssue;


public interface JiraMetricService
{
	void insertJiraMetricIssue( JiraMetricIssue jiraMetricIssue );


	JiraMetricIssue findJiraMetricIssueById( Long id );


	JiraMetricIssue findJiraMetricIssueByPK( JiraMetricIssue jiraMetricIssue );


	List<JiraMetricIssue> findJiraMetricIssuesByStatus( String status );


	List<JiraMetricIssue> findJiraMetricIssuesByIssueType( String issueType );


	List<JiraMetricIssue> findJiraMetricIssuesByProjectKey( String projectKey );


	List<JiraMetricIssue> findJiraMetricIssuesByReporterName( String reporterName );


	List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName( String assigneeName );


	List<JiraMetricIssue> findJiraMetricIssuesByResolution( String resolution );


	void updateJiraMetricIssue( JiraMetricIssue jiraMetricIssue );


	void deleteJiraMetricIssue( JiraMetricIssue jiraMetricIssue );
}
