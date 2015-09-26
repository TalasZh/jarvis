package org.safehaus.service.api;


import java.util.List;

import org.safehaus.dao.entities.jira.JiraMetricIssue;


public interface JiraMetricService
{
	void insertJiraMetricIssue( JiraMetricIssue jiraMetricIssue );

	JiraMetricIssue findJiraMetricIssueById( Long id );

	List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName( String assigneeName );

	void updateJiraMetricIssue( JiraMetricIssue jiraMetricIssue );

	void deleteJiraMetricIssue( JiraMetricIssue jiraMetricIssue );

	void batchInsert(List<JiraMetricIssue> issues);
}
