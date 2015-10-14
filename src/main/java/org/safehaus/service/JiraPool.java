package org.safehaus.service;


import java.util.List;
import java.util.Set;

import org.safehaus.analysis.service.JiraConnector;
import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.jira.JiraProject;
import org.safehaus.dao.entities.jira.JiraUser;
import org.safehaus.dao.entities.jira.ProjectVersion;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.service.api.JiraMetricDao;
import org.safehaus.timeline.TimelineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Project;
import net.rcarz.jiraclient.User;
import net.rcarz.jiraclient.Version;

import static org.safehaus.Constants.MAX_RESULTS;


/**
 * Created by talas on 10/13/15.
 */
public class JiraPool
{
    private static final Logger logger = LoggerFactory.getLogger( JiraPool.class );

    @Autowired
    private JiraMetricDao jiraMetricDao;

    @Autowired
    private TimelineManager timelineManager;

    @Autowired
    private JiraConnector jiraConnector;

    @Autowired
    private StashPool stashPool;


    public List<String> getJiraProjectKeys()
    {
        List<String> projectKeys = Lists.newArrayList();

        JiraClient jiraRestClient;
        try
        {
            jiraRestClient = jiraConnector.getJiraClient();
        }
        catch ( JiraClientException e )
        {
            logger.error( "Error connecting to JIRA.", e );
            return projectKeys;
        }

        // Get all project names to use on getIssues
        try
        {
            List<net.rcarz.jiraclient.Project> jiraProjects = jiraRestClient.getProjects();
            logger.info( "Printing all projects" );
            for ( Project project : jiraProjects )
            {
                projectKeys.add( project.getKey() );
                try
                {
                    getProjectDetails( jiraRestClient, project );
                }
                catch ( Exception e )
                {
                    logger.error( "Error fetching project information." );
                }
            }
        }
        catch ( Exception e )
        {
            logger.error( "Could not get all the projects.", e );
        }
        return projectKeys;
    }


    private void getProjectDetails( JiraClient jiraRestClient, Project project ) throws JiraException
    {
        net.rcarz.jiraclient.Project projectDetails = jiraRestClient.getProject( project.getKey() );
        List<ProjectVersion> projectVersionList = Lists.newArrayList();
        for ( final Version version : projectDetails.getVersions() )
        {
            ProjectVersion projectVersion =
                    new ProjectVersion( version.getId(), version.getDescription(), version.getName(),
                            version.getReleaseDate() );

            projectVersionList.add( projectVersion );
        }

        JiraProject jiraProject =
                new JiraProject( projectDetails.getId(), projectDetails.getKey(), projectDetails.getAssigneeType(),
                        projectDetails.getDescription(), projectDetails.getName(), projectVersionList );

        jiraMetricDao.saveProject( jiraProject );
    }


    public void pullRecentUpdates()
    {
        JiraClient jiraClient;
        try
        {
            jiraClient = jiraConnector.getJiraClient();
        }
        catch ( JiraClientException e )
        {
            logger.error( "Error connecting to JIRA.", e );
            return;
        }

        String jql = "created >= -10m OR updated >= -10m";
        Set<String> projectKeys = searchJira( jql, jiraClient );
        for ( final String projectKey : projectKeys )
        {
            timelineManager.rebuildProjectStructure( projectKey );
        }

        if ( jiraClient != null )
        {
            jiraConnector.destroy();
        }
    }


    /**
     * returns list of project keys from pulled issues
     */
    private Set<String> searchJira( String jql, JiraClient jiraClient )
    {
        Set<String> projectKeys = Sets.newHashSet();
        Integer startIndex = 0;
        Integer total = 0;

        Issue.SearchResult searchResult = null;
        try
        {
            searchResult = jiraClient.searchIssues( jql, "*all", "changelog", MAX_RESULTS, startIndex );
        }
        catch ( JiraException e )
        {
            logger.error( "Error pulling JIRA issues", e );
        }

        if ( searchResult == null )
        {
            return projectKeys;
        }

        total = searchResult.total + MAX_RESULTS;

        while ( total >= MAX_RESULTS + startIndex )
        {
            for ( final Issue issue : searchResult.issues )
            {
                saveUser( issue.getAssignee() );
                saveUser( issue.getReporter() );

                logger.info( "Preparing issues for jarvis format..." );
                JiraMetricIssue issueToAdd = new JiraMetricIssue( issue );

                jiraMetricDao.insertJiraMetricIssue( issueToAdd );

                if ( issue.getProject() != null && jiraMetricDao.getProject( issue.getProject().getKey() ) == null )
                {
                    projectKeys.add( issue.getProject().getKey() );
                    try
                    {
                        getProjectDetails( jiraClient, issue.getProject() );
                    }
                    catch ( JiraException e )
                    {
                        logger.error( "Exception pulling project data", e );
                    }
                }
            }

            startIndex += MAX_RESULTS;
            try
            {
                searchResult = jiraClient.searchIssues( jql, "*all", "changelog", MAX_RESULTS, startIndex );
            }
            catch ( JiraException e )
            {
                logger.error( "Error pulling JIRA issues", e );
            }
        }
        return projectKeys;
    }


    public void getJiraIssues()
    {
        JiraClient jiraClient;
        try
        {
            jiraClient = jiraConnector.getJiraClient();
        }
        catch ( JiraClientException e )
        {
            logger.error( "Error connecting to JIRA.", e );
            return;
        }

        String jql = "";
        searchJira( jql, jiraClient );
        timelineManager.init();
        stashPool.getStashCommits();

        if ( jiraClient != null )
        {
            jiraConnector.destroy();
        }
    }


    private void saveUser( final User user )
    {
        if ( user != null )
        {
            JiraUser assigneeUser =
                    new JiraUser( user.getId(), user.getDisplayName(), user.getEmail(), user.getName() );
            jiraMetricDao.insertUser( assigneeUser );
        }
    }
}
