package org.safehaus.service;


import java.util.List;

import org.safehaus.jira.api.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisMember;
import org.safehaus.model.JarvisProject;

import com.atlassian.jira.rest.client.api.domain.Project;


/**
 * Created by tzhamakeev on 5/19/15.
 */
public interface JiraManager
{
    List<JarvisMember> getProjectMemebers(String projectId) throws JiraClientException;

    List<JarvisIssue> getIssues( String projectId );

    JarvisProject getProject( String projectId ) throws JiraClientException;

    List<JarvisProject> getProjects();

    JarvisIssue getIssue( String issueId );

    JarvisIssue createIssue( JarvisIssue issue, String token );
}
