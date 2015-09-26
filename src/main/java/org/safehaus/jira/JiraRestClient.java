package org.safehaus.jira;


import java.util.List;

import org.safehaus.dao.entities.jira.JarvisIssue;
import org.safehaus.dao.entities.jira.JarvisMember;
import org.safehaus.exceptions.JiraClientException;

import net.rcarz.jiraclient.Component;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Project;
import net.rcarz.jiraclient.Status;
import net.rcarz.jiraclient.Transition;


/**
 * Created by tzhamakeev on 5/5/15.
 */
public interface JiraRestClient
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

    Status changeStatus( String issueIdOrKey, int transitonId ) throws JiraClientException;

    public List<Component> getAllComponents( String projectId ) throws JiraClientException;

    public Issue getIssue( String issueKey );

    void updateIssueState( String issueKeyOrId, Integer transitionId );

    void startIssue( String issueKeyOrId ) throws JiraClientException;

    void resolveIssue( String issueKeyOrId ) throws JiraClientException;

    Iterable<Transition> getTransitions( String issueKeyOrId ) throws JiraClientException;

    Issue createIssue( JarvisIssue jarvisIssue ) throws JiraClientException;

    public List<JarvisMember> getProjectMembers( String projectId ) throws JiraClientException;

    List<Issue> getIssues( String projectId );

//    Status storyStart( String issueIdOrKey ) throws JiraClientException;

    Status changeStatus( String issueIdOrKey, String actionName ) throws JiraClientException;

    //    Issue createIssue( JarvisIssue issue, String token ) throws JiraClientException;
}
