package org.safehaus.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.safehaus.service.JiraClient;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisIssueType;
import org.safehaus.model.JarvisLink;
import org.safehaus.model.JarvisMember;
import org.safehaus.model.JarvisProject;
import org.safehaus.service.JiraManager;
import org.safehaus.util.JarvisContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueLink;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;


public class JiraManagerImpl implements JiraManager
{
    private static Logger logger = LoggerFactory.getLogger( JiraManagerImpl.class );


    @Override
    public List<JarvisMember> getProjectMemebers( final String projectId ) throws JiraClientException
    {
        return getJiraClient() == null ? null : getJiraClient().getProjectMemebers( projectId.toString() );
    }


    @Override
    public List<JarvisIssue> getIssues( final String projectId ) throws JiraClientException
    {
        return getJiraClient().getIssues( projectId );
    }


    @Override
    public JarvisProject getProject( final String projectId ) throws JiraClientException


    {
        Project project = getJiraClient().getProject( projectId );
        List<String> types = new ArrayList<>();

        for ( Iterator<IssueType> iterator = project.getIssueTypes().iterator(); iterator.hasNext(); )
        {
            types.add( iterator.next().getName() );
        }
        return new JarvisProject( project.getId(), project.getKey(), project.getName(), project.getDescription(),
                types );
    }


    @Override
    public List<JarvisProject> getProjects() throws JiraClientException
    {
        List<Project> projects = getJiraClient().getAllProjects();
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
    public JarvisIssue getIssue( final String issueId ) throws JiraClientException
    {
        Issue issue = getJiraClient().getIssue( issueId );

        JarvisIssue result = buildJarvisIssue( issue );
        return result;
    }


    @Override
    public JarvisIssue createIssue( final JarvisIssue issue ) throws JiraClientException


    {
        Issue jiraIssue = getJiraClient().createIssue( issue );
        return buildJarvisIssue( jiraIssue );
    }


    @Override
    public void buildBlocksChain( final String issueId, List<JarvisIssue> chain ) throws JiraClientException
    {

        JarvisIssue issue = getIssue( issueId );

        JarvisLink blockedIssueLink = issue.getLink( JiraClient.BLOCKS_LINK_NAME, JiraClient.OUTBOUND );
        //        logger.debug( String.format( "%s %s", issue.getKey(),
        //                blockedIssueLink != null ? blockedIssueLink.getKey() + ":" + blockedIssueLink.getLinkType()
        // + blockedIssueLink.getLinkDirection() : null ) );
        if ( blockedIssueLink != null )
        {
            buildBlocksChain( blockedIssueLink.getKey(), chain );
        }

        chain.add( issue );
    }


    @Override
    public void startIssue( String issueKeyOrId ) throws JiraClientException
    {
        getJiraClient().startIssue( issueKeyOrId );
    }


    @Override
    public void resolveIssue( String issueKeyOrId ) throws JiraClientException
    {
        getJiraClient().resolveIssue( issueKeyOrId );
    }


    public void destroy() throws IOException, JiraClientException
    {
        getJiraClient().close();
    }


    private JarvisIssue buildJarvisIssue( Issue issue ) throws JiraClientException
    {
        if ( issue == null )
        {
            return new JarvisIssue();
        }
        List<JarvisLink> links = new ArrayList<>();
        for ( Iterator<IssueLink> iterator = issue.getIssueLinks().iterator(); iterator.hasNext(); )
        {
            IssueLink link = iterator.next();

            Issue i = getJiraClient().getIssue( link.getTargetIssueKey() );
            links.add( new JarvisLink( i.getId(), link.getTargetIssueKey(), link.getIssueLinkType().getName(),
                    link.getIssueLinkType().getDirection().name(),
                    new JarvisIssueType( i.getIssueType().getId(), i.getIssueType().getName() ) ) );
        }
        return new JarvisIssue( issue.getId(), issue.getKey(), issue.getSummary(),
                new JarvisIssueType( issue.getIssueType().getId(), issue.getIssueType().getName() ),
                issue.getDescription(), issue.getTimeTracking() != null ? ( issue.getTimeTracking() != null ?
                                                                            issue.getTimeTracking()
                                                                                 .getRemainingEstimateMinutes()
                                                                                    != null ? issue.getTimeTracking()
                                                                                                   .getRemainingEstimateMinutes()
                                                                                                   .toString() : null :
                                                                            null ) : null,
                issue.getAssignee() != null ? issue.getAssignee().getName() : null,
                issue.getReporter() != null ? issue.getReporter().getName() : null, issue.getComponents().toString(),
                issue.getLabels().toString(), issue.getStatus().getName(),
                issue.getResolution() != null ? issue.getResolution().getName() : null,
                issue.getFixVersions() != null ? issue.getFixVersions().toString() : null,
                issue.getCreationDate().toString(), links, issue.getProject().getKey() );
    }


    private JiraClient getJiraClient() throws JiraClientException
    {
        return JarvisContextHolder.getContext().getJiraClient();
    }
}
