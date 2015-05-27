package org.safehaus.jira.api;


import java.io.IOException;
import java.util.List;

import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisMember;

import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;


/**
 * Created by tzhamakeev on 5/5/15.
 */
public interface JiraClient
{
    public List<Project> getAllProjects();

    public Project getProject( String projectId ) throws JiraClientException;

    public List<Issue> getIssues( String projectId, String componentId, int maxResult, int startIndex,
                                  final String issueType ) throws JiraClientException;

    public List<Issue> getIssues( String projectId, int maxResult, int startIndex, final String issueType )
            throws JiraClientException;

    public List<Component> getAllComponents( String projectId ) throws JiraClientException;

    public Issue getIssue( String issueKey );

    public void close() throws IOException;

    public List<JarvisMember> getProjectMemebers( String projectId ) throws JiraClientException;

    List<JarvisIssue> getIssues( String projectId );

    Issue createIssue( JarvisIssue issue, String token ) throws JiraClientException;
}
