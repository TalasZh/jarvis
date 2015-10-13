package org.safehaus.service;


import java.util.Map;
import java.util.Set;

import org.safehaus.analysis.service.StashConnector;
import org.safehaus.dao.entities.stash.Commit;
import org.safehaus.dao.entities.stash.StashMetricIssue;
import org.safehaus.jira.IssueKeyParser;
import org.safehaus.service.api.JiraMetricDao;
import org.safehaus.service.api.StashMetricService;
import org.safehaus.stash.client.Page;
import org.safehaus.stash.client.StashClient;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.stash.model.Change;
import org.safehaus.stash.model.Project;
import org.safehaus.stash.model.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import static org.safehaus.Constants.MAX_RESULTS;


/**
 * Created by talas on 10/13/15.
 */
public class StashPool
{
    @Autowired
    private StashMetricService stashMetricService;

    @Autowired
    private JiraMetricDao jiraMetricDao;

    @Autowired
    private StashConnector stashConnector;

    private static final Logger logger = LoggerFactory.getLogger( StashPool.class );


    public void getStashCommits()
    {
        StashClient stashMan;
        try
        {
            stashMan = stashConnector.stashConnect();
        }
        catch ( StashManagerException e )
        {
            logger.error( "Couldn't establish stash connection", e );
            return;
        }

        Map<Project, Set<Repository>> projectRepoMap = Maps.newHashMap();

        Set<Project> stashProjectSet;
        stashProjectSet = getProjects( stashMan );
        for ( Project p : stashProjectSet )
        {
            Set<Repository> repos = getProjectRepositories( stashMan, p );
            projectRepoMap.put( p, repos );
        }

        for ( final Map.Entry<Project, Set<Repository>> entry : projectRepoMap.entrySet() )
        {
            for ( final Repository repository : entry.getValue() )
            {
                pullProjectRepoCommits( stashMan, entry.getKey(), repository.getSlug() );
            }
        }
    }


    private Set<Project> getProjects( StashClient stashMan )
    {
        Set<Project> projects = Sets.newHashSet();
        // Get all projects

        int startIndex = 0;
        Page<Project> page = null;

        do
        {
            try
            {
                page = stashMan.getProjects( MAX_RESULTS, startIndex );
                if ( page != null )
                {
                    projects.addAll( page.getValues() );
                    startIndex += MAX_RESULTS;
                }
            }
            catch ( StashManagerException e )
            {
                logger.error( "Error pulling stash projects", e );
            }
        }
        while ( page != null && !page.isLastPage() );

        return projects;
    }


    private Set<Repository> getProjectRepositories( StashClient stashClient, Project project )
    {
        Set<Repository> repoSet = Sets.newHashSet();

        String projectKey = project.getKey();
        int startIndex = 0;

        Page<Repository> repoPage = null;
        do
        {
            try
            {
                repoPage = stashClient.getRepos( projectKey, MAX_RESULTS, startIndex );
                if ( repoPage != null )
                {
                    repoSet.addAll( repoPage.getValues() );
                    startIndex += MAX_RESULTS;
                }
            }
            catch ( StashManagerException e )
            {
                logger.error( "StashManagerException exception while pulling repository list" );
            }
        }
        while ( repoPage != null && !repoPage.isLastPage() );

        return repoSet;
    }


    private void pullProjectRepoCommits( StashClient stashClient, Project project, String repoSlug )
    {
        Page<Commit> commitPage = null;
        int startIndex = 0;
        do
        {
            try
            {
                commitPage = stashClient.getCommits( project.getKey(), repoSlug, MAX_RESULTS, startIndex );
                if ( commitPage != null )
                {
                    startIndex += MAX_RESULTS;
                    for ( final Commit commit : commitPage.getValues() )
                    {
                        Set<Change> changes =
                                getCommitChanges( stashClient, project.getKey(), repoSlug, commit.getId() );
                        for ( final Change change : changes )
                        {
                            StashMetricIssue stashMetricIssue = new StashMetricIssue();
                            stashMetricIssue.setPath( change.getPath() );
                            stashMetricIssue.setAuthor( commit.getAuthor() );

                            stashMetricIssue.setAuthorTimestamp( commit.getAuthorTimestamp() );
                            stashMetricIssue.setId( change.getContentId() );

                            stashMetricIssue.setNodeType( change.getNodeType() );
                            stashMetricIssue.setPercentUnchanged( change.getPercentUnchanged() );
                            stashMetricIssue.setProjectName( project.getName() );
                            stashMetricIssue.setProjectKey( project.getKey() );
                            stashMetricIssue.setSrcPath( change.getSrcPath() );
                            stashMetricIssue.setType( change.getType() );
                            stashMetricIssue.setCommitMessage( commit.getMessage() );

                            if ( change.getLink() != null )
                            {
                                stashMetricIssue.setUri( stashClient.getBaseUrl() + change.getLink().getUrl() );
                            }

                            stashMetricService.insertStashMetricIssue( stashMetricIssue );
                            associateCommitToIssue( stashMetricIssue );
                        }
                    }
                }
            }
            catch ( StashManagerException e )
            {
                logger.error( "Error pulling repo commits", e );
            }
        }
        while ( commitPage != null && !commitPage.isLastPage() );
    }


    private Set<Change> getCommitChanges( StashClient stashClient, String projectKey, String repoSlug, String commitId )
    {
        Set<Change> changes = Sets.newHashSet();

        Page<Change> changePage = null;
        int startIndex = 0;
        do
        {
            try
            {
                changePage = stashClient.getCommitChanges( projectKey, repoSlug, commitId, MAX_RESULTS, startIndex );
                if ( changePage != null )
                {
                    changes.addAll( changePage.getValues() );
                    startIndex += MAX_RESULTS;
                }
            }
            catch ( StashManagerException e )
            {
                logger.error( "Error pulling changes for commit", e );
            }
        }
        while ( changePage != null && !changePage.isLastPage() );

        return changes;
    }


    private void associateCommitToIssue( StashMetricIssue stashMetricIssue )
    {
        String commitMsg = stashMetricIssue.getCommitMessage();
        Set<IssueKeyParser> issues = Sets.newHashSet( IssueKeyParser.parseIssueKeys( commitMsg ) );
        for ( final IssueKeyParser issue : issues )
        {
            jiraMetricDao.attachCommit( issue.getFullyQualifiedIssueKey(), stashMetricIssue.getId() );
        }
    }
}
