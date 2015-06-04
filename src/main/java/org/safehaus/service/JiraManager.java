package org.safehaus.service;


import java.util.List;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisMember;
import org.safehaus.model.JarvisProject;


/**
 * Created by tzhamakeev on 5/19/15.
 */
public interface JiraManager
{
    List<JarvisMember> getProjectMemebers( String projectId ) throws JiraClientException;

    List<JarvisIssue> getIssues( String projectId ) throws JiraClientException;

    JarvisProject getProject( String projectId ) throws JiraClientException;

    List<JarvisProject> getProjects() throws JiraClientException;

    JarvisIssue getIssue( String issueId ) throws JiraClientException;

    //    JarvisIssue createIssue( JarvisIssue issue, String token ) throws JiraClientException;

    JarvisIssue createIssue( JarvisIssue issue ) throws JiraClientException;

    void buildBlocksChain( String issueId, List<JarvisIssue> chain ) throws JiraClientException;

    void startIssue( String issueKeyOrId ) throws JiraClientException;

    void resolveIssue( String issueKeyOrId ) throws JiraClientException;

    //    void buildBlocksTree( String issueId, List<JarvisIssue> chain );
}
