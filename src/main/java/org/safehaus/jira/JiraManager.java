package org.safehaus.jira;


import java.util.List;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.model.JarvisIssue;
import org.safehaus.jira.model.JarvisMember;
import org.safehaus.model.JarvisProject;

import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.Transition;


/**
 * Created by tzhamakeev on 5/19/15.
 */
public interface JiraManager
{
    String STORY_START_ACTION_NAME = "Start";
    String STORY_REQUEST_APPROVAL_ACTION_NAME = "Send for Approval";
    String STORY_RESOLVE_ACTION_NAME = "Send to Close";
    String STORY_APPROVE_ACTION_NAME = "Approve";
    String STORY_REJECT_ACTION_NAME = "Reject";
    String ISSUE_REOPEN_ACTION_NAME = "Reopen";

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

    Iterable<Transition> getTransitions( String issueIdOrKey ) throws JiraClientException;

    Status toTransition( String issueIdOrKey, String id ) throws JiraClientException;

    Status storyStart( String issueIdOrKey ) throws JiraClientException;

    Status storyRequestApproval( String issueIdOrKey ) throws JiraClientException;

    Status storyResolve( String issueIdOrKey ) throws JiraClientException;

    Status storyApprove( String issueIdOrKey ) throws JiraClientException;

    Status storyReject( String issueIdOrKey ) throws JiraClientException;

    Status reopenIssue( String id ) throws JiraClientException;
}
