package org.safehaus.stash.client;


import java.util.Set;

import javax.ws.rs.core.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.model.JarvisContext;
import org.safehaus.stash.TestUtil;
import org.safehaus.stash.model.Activity;
import org.safehaus.stash.model.Branch;
import org.safehaus.stash.model.BuildStats;
import org.safehaus.stash.model.BuildStatus;
import org.safehaus.stash.model.Change;
import org.safehaus.stash.model.ChangeSet;
import org.safehaus.stash.model.Commit;
import org.safehaus.stash.model.Event;
import org.safehaus.stash.model.Group;
import org.safehaus.stash.model.JiraIssue;
import org.safehaus.stash.model.Project;
import org.safehaus.stash.model.PullRequest;
import org.safehaus.stash.model.PullRequestState;
import org.safehaus.stash.model.Repository;
import org.safehaus.stash.util.AtlassianRestUtil;
import org.safehaus.util.JarvisContextHolder;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


@RunWith( MockitoJUnitRunner.class )
public class StashManagerImplTest
{
    @Mock
    AtlassianRestUtil atlassianRestUtil;

    StashManagerImpl stashManager;


    @Before
    public void setUp() throws Exception
    {
        //for integration test set the context to valid crowd.token_key
        JarvisContextHolder
                .setContext( new JarvisContext( "", new Cookie( "crowd.token_key", "c5EKHJvcroxWheWeaA5m9g00" ) ) );
        stashManager = spy( new StashManagerImpl( TestUtil.STASH_URL ) );
    }


    private void setRestResponse( String response ) throws AtlassianRestUtil.RestException, StashManagerException
    {
        doReturn( response ).when( stashManager ).get( anyString(), anyVararg() );
    }


    @Test
    public void testGetProjects() throws Exception
    {
        setRestResponse( TestUtil.STASH_PROJECTS_JSON );

        Page<Project> projects = stashManager.getProjects( 4, 0 );

        assertTrue( projects.getSize() == 4 );
    }


    @Test
    public void testGetProject() throws Exception
    {
        setRestResponse( TestUtil.STASH_PROJECT_JSON );

        Project project = stashManager.getProject( TestUtil.PROJECT_KEY );

        assertNotNull( project );
    }


    @Test
    public void testGetPermittedGroups() throws Exception
    {
        setRestResponse( TestUtil.STASH_GROUP_JSON );

        Page<Group> groups = stashManager.getPermittedGroups( TestUtil.PROJECT_KEY, 2, 0 );

        assertTrue( groups.getSize() == 2 );
    }


    @Test
    public void testGetRepos() throws Exception
    {
        setRestResponse( TestUtil.STASH_REPOS_JSON );

        Page<Repository> repos = stashManager.getRepos( TestUtil.PROJECT_KEY, 1, 0 );

        assertTrue( repos.getSize() > 0 );

        System.out.println( repos );
    }


    @Test
    public void testGetRepo() throws Exception
    {
        setRestResponse( TestUtil.STASH_REPO_JSON );

        Repository repository = stashManager.getRepo( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG );

        assertNotNull( repository );
    }


    @Test
    public void testGetPullRequests() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUESTS_JSON );

        Page<PullRequest> pullRequests = stashManager
                .getPullRequests( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, TestUtil.MASTER_BRANCH,
                        PullRequestState.DECLINED, 3, 0 );

        assertTrue( pullRequests.getSize() > 0 );
    }


    @Test
    public void testGetPullRequest() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUEST_JSON );

        PullRequest pullRequest = stashManager.getPullRequest( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1 );

        assertNotNull( pullRequest );
    }


    @Test
    public void testGetPRActivities() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUEST_ACTIVITY_JSON );

        Page<Activity> activities =
                stashManager.getPullRequestActivities( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1, 1, 0 );

        assertTrue( activities.getSize() > 0 );
    }


    @Test
    public void testGetPrCommits() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUEST_COMMITS_JSON );

        Page<Commit> commits = stashManager.getPullRequestCommits( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1, 1, 0 );

        assertTrue( commits.getSize() > 0 );
    }


    @Test
    public void testBranches() throws Exception
    {
        setRestResponse( TestUtil.STASH_BRANCHES_JSON );

        Page<Branch> branches = stashManager.getBranches( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1, 0 );

        assertTrue( branches.getSize() > 0 );
    }


    @Test
    public void testGetDefaultBranch() throws Exception
    {
        setRestResponse( TestUtil.STASH_BRANCH_JSON );

        Branch defaultBranch = stashManager.getDefaultBranch( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG );

        assertNotNull( defaultBranch );
    }


    @Test
    public void testGetPRChanges() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUEST_CHANGES_JSON );

        Page<Change> changes =
                stashManager.getPullRequestChanges( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 21, 10, 0 );

        assertTrue( changes.getSize() > 0 );
    }


    @Test
    public void testGetChangesBetweenCommits() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUEST_CHANGES_JSON );

        Page<Change> changes = stashManager
                .getChangesBetweenCommits( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, "683ade1", "3353762", 10, 0 );

        assertTrue( changes.getSize() > 0 );
    }


    @Test
    public void testGetCommits() throws Exception
    {
        setRestResponse( TestUtil.STASH_COMMITS_JSON );

        Page<Commit> commits = stashManager.getCommits( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 10, 0 );

        assertFalse( commits.getValues().isEmpty() );
    }


    @Test
    public void testGetCommit() throws Exception
    {
        setRestResponse( TestUtil.STASH_COMMIT_JSON );

        Commit commit = stashManager.getCommit( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, "ad31de9" );

        assertNotNull( commit );
    }


    @Test
    public void testGetCommitChanges() throws Exception
    {
        setRestResponse( TestUtil.STASH_COMMIT_CHANGES_JSON );

        Page<Change> changes =
                stashManager.getCommitChanges( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, "2fda08f", 10, 0 );

        assertFalse( changes.getValues().isEmpty() );
    }


    @Test
    public void testGetProjectEvents() throws Exception
    {
        setRestResponse( TestUtil.STASH_EVENTS_JSON );

        Page<Event> events = stashManager.getProjectEvents( TestUtil.PROJECT_KEY, 10, 0 );

        assertFalse( events.getValues().isEmpty() );
    }


    @Test
    public void testGetRepoEvents() throws Exception
    {
        setRestResponse( TestUtil.STASH_EVENTS_JSON );

        Page<Event> events = stashManager.getRepoEvents( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 10, 0 );

        assertFalse( events.getValues().isEmpty() );
    }


    @Test
    public void testGetCommitBuildStatistics() throws Exception
    {
        setRestResponse( TestUtil.STASH_BUILD_STATISTICS_JSON );

        BuildStats buildStats = stashManager.getCommitBuildStatistics( "2fda08f" );

        assertNotNull( buildStats );
    }


    @Test
    public void testGetCommitBuildStatuses() throws Exception
    {
        setRestResponse( TestUtil.STASH_BUILD_STATUSES_JSON );

        Page<BuildStatus> buildStatuses = stashManager.getCommitBuildStatuses( "2fda08f", 10, 0 );

        assertFalse( buildStatuses.getValues().isEmpty() );
    }


    @Test
    public void testGetJiraIssuesByPullRequest() throws Exception
    {
        setRestResponse( TestUtil.STASH_JIRA_ISSUES_BY_PULL_REQUEST_JSON );

        Set<JiraIssue> jiraIssues =
                stashManager.getJiraIssuesByPullRequest( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1 );

        assertFalse( jiraIssues.isEmpty() );
    }


    @Test
    public void testGetChangesByJiraIssue() throws Exception
    {
        setRestResponse( TestUtil.STASH_CHANGES_BY_JIRA_ISSUE_JSON );

        Page<ChangeSet> jiraIssueChangePage = stashManager.getChangesByJiraIssue( "HUB-100", 1, 0, 10 );

        assertNotNull( jiraIssueChangePage );
    }
}
