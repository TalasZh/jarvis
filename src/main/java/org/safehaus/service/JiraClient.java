package org.safehaus.service;


import java.io.IOException;
import java.util.List;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisMember;

import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Transition;


/**
 * Created by tzhamakeev on 5/5/15.
 */
public interface JiraClient
{
    public static final String BLOCKS_LINK_NAME = "Blocks";
    public static final String OUTBOUND = "OUTBOUND";
    public static final String INBOUND = "INBOUND";

    public List<Project> getAllProjects();

    public Project getProject( String projectId ) throws JiraClientException;

    public List<Issue> getIssues( String projectId, String componentId, int maxResult, int startIndex,
                                  final String issueType ) throws JiraClientException;

    public List<Issue> getIssues( String projectId, int maxResult, int startIndex, final String issueType )
            throws JiraClientException;

    public List<Component> getAllComponents( String projectId ) throws JiraClientException;

    public Issue getIssue( String issueKey );

//    void updateIssueState( String issueKeyOrId, Integer transitionId );

    void startIssue( String issueKeyOrId ) throws JiraClientException;

    void resolveIssue( String issueKeyOrId ) throws JiraClientException;

    Iterable<Transition> getTransitions( String issueKeyOrId ) throws JiraClientException;

    public void close() throws IOException;

    Issue createIssue( JarvisIssue jarvisIssue ) throws JiraClientException;

    public List<JarvisMember> getProjectMemebers( String projectId ) throws JiraClientException;

    List<JarvisIssue> getIssues( String projectId );

    //    Issue createIssue( JarvisIssue issue, String token ) throws JiraClientException;
}
