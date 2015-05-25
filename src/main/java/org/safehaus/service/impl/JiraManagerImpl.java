package org.safehaus.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.safehaus.jira.api.JiraClient;
import org.safehaus.jira.api.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisLink;
import org.safehaus.model.JarvisMember;
import org.safehaus.model.JarvisProject;
import org.safehaus.service.JiraManager;
import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;


public class JiraManagerImpl implements JiraManager
{

    JiraClient jiraClient;


    @Autowired
    public void setJiraClient( final JiraClient jiraClient )
    {
        this.jiraClient = jiraClient;
    }


    @Override
    public List<JarvisMember> getProjectMemebers( final String projectId ) throws JiraClientException
    {
        return jiraClient == null ? null : jiraClient.getProjectMemebers( projectId.toString() );
    }


    @Override
    public List<JarvisIssue> getIssues( final String projectId )
    {
        return jiraClient.getIssues( projectId );
    }


    @Override
    public JarvisProject getProject( final String projectId ) throws JiraClientException
    {
        Project project = jiraClient.getProject( projectId );
        List<String> types = new ArrayList<>();

        for ( Iterator<IssueType> iterator = project.getIssueTypes().iterator(); iterator.hasNext(); )
        {
            types.add( iterator.next().getName() );
        }
        return new JarvisProject( project.getId(), project.getKey(), project.getName(), project.getDescription(),
                types );
    }


    @Override
    public List<JarvisProject> getProjects()
    {
        List<Project> projects = jiraClient.getAllProjects();
        List<JarvisProject> result = new ArrayList<>();

        for ( Project project : projects )
        {

            List<String> types = new ArrayList<>();

            for ( Iterator<IssueType> iterator = project.getIssueTypes().iterator(); iterator.hasNext(); )
            {
                types.add( iterator.next().getName() );
            }
            result.add(
                    new JarvisProject( project.getId(), project.getKey(), project.getName(), project.getDescription(),
                            types ) );
        }
        return result;
    }


    @Override
    public JarvisIssue getIssue( final String issueId )
    {
        Issue issue = jiraClient.getIssue( issueId );

        List<JarvisLink> links = new ArrayList<>();
        for ( Iterator<IssueLink> iterator = issue.getIssueLinks().iterator(); iterator.hasNext(); )
        {
            IssueLink link = iterator.next();

            Issue i = jiraClient.getIssue( link.getTargetIssueKey() );
            links.add( new JarvisLink( link.getTargetIssueKey(), link.getIssueLinkType().getName(),
                    i.getIssueType().getName() ) );
        }

        JarvisIssue result =
                new JarvisIssue( issue.getId(), issue.getKey(), issue.getSummary(), issue.getIssueType().getName(),
                        issue.getDescription(), issue.getTimeTracking() != null ? ( issue.getTimeTracking() != null ?
                                                                                    issue.getTimeTracking()
                                                                                         .getRemainingEstimateMinutes()
                                                                                         .toString() : null ) : null,
                        issue.getAssignee() != null ? issue.getAssignee().getName() : null,
                        issue.getReporter() != null ? issue.getReporter().getName() : null,
                        issue.getComponents().toString(), issue.getLabels().toString(), issue.getStatus().getName(),
                        issue.getResolution() != null ? issue.getResolution().getName() : null,
                        issue.getFixVersions() != null ? issue.getFixVersions().toString() : null,
                        issue.getCreationDate().toString(), links, issue.getProject().getKey() );
        return result;
    }


    public void destroy() throws IOException
    {
        jiraClient.close();
    }
}
