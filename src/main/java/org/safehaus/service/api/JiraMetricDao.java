package org.safehaus.service.api;


import java.util.List;

import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.jira.JiraProject;
import org.safehaus.dao.entities.jira.JiraUser;


public interface JiraMetricDao
{
    void insertUser( JiraUser jiraUser );

    JiraUser getJiraUser( String userId );

    JiraUser getJiraUserByUsername( String username );

    void insertJiraMetricIssue( JiraMetricIssue jiraMetricIssue );

    JiraMetricIssue findJiraMetricIssueById( Long id );

    List<JiraMetricIssue> findJiraMetricIssuesByAssigneeName( String assigneeName );

    void updateJiraMetricIssue( JiraMetricIssue jiraMetricIssue );

    void deleteJiraMetricIssue( JiraMetricIssue jiraMetricIssue );

    void batchInsert( List<JiraMetricIssue> issues );

    List<JiraProject> getProjects();

    JiraProject getProject( String projectKey );

    void saveProject( JiraProject project );

    void updateProject( JiraProject project );

    void deleteProject( JiraProject project );

    List<JiraMetricIssue> getProjectIssues( String projectKey );
}
