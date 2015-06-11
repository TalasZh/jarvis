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
import org.safehaus.stash.model.BuildStatistics;
import org.safehaus.stash.model.BuildStatus;
import org.safehaus.stash.model.Change;
import org.safehaus.stash.model.Commit;
import org.safehaus.stash.model.Event;
import org.safehaus.stash.model.Group;
import org.safehaus.stash.model.Project;
import org.safehaus.stash.model.PullRequest;
import org.safehaus.stash.model.Repo;
import org.safehaus.stash.util.RestUtil;
import org.safehaus.util.JarvisContextHolder;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith( MockitoJUnitRunner.class )
public class StashManagerImplTest
{
    @Mock
    RestUtil restUtil;

    StashManagerImpl stashManager = new StashManagerImpl( TestUtil.STASH_URL );


    @Before
    public void setUp() throws Exception
    {
        //for integration test set the context to valid crowd.token_key
        JarvisContextHolder
                .setContext( new JarvisContext( "", new Cookie( "crowd.token_key", "xY33X50y7qPE5O6DqzsMNg00" ) ) );
    }


    private void setRestResponse( String response ) throws RestUtil.RestException
    {
        stashManager.restUtil = restUtil;
        when( restUtil.get( anyString(), anyMap() ) ).thenReturn( response );
    }


    @Test
    public void testGetProjects() throws Exception
    {
        setRestResponse( TestUtil.STASH_PROJECTS_JSON );

        Set<Project> projects = stashManager.getProjects( 4 );

        assertTrue( projects.size() == 4 );
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

        Set<Group> groups = stashManager.getPermittedGroups( TestUtil.PROJECT_KEY, 2 );

        assertTrue( groups.size() == 2 );
    }


    @Test
    public void testGetRepos() throws Exception
    {
        setRestResponse( TestUtil.STASH_REPOS_JSON );

        Set<Repo> repos = stashManager.getRepos( TestUtil.PROJECT_KEY, 1 );

        assertFalse( repos.isEmpty() );
    }


    @Test
    public void testGetRepo() throws Exception
    {
        setRestResponse( TestUtil.STASH_REPO_JSON );

        Repo repo = stashManager.getRepo( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG );

        assertNotNull( repo );
    }


    @Test
    public void testGetPullRequests() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUESTS_JSON );

        Set<PullRequest> pullRequests = stashManager
                .getPullRequests( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, TestUtil.MASTER_BRANCH,
                        PullRequest.State.DECLINED, 3 );

        assertFalse( pullRequests.isEmpty() );
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

        Set<Activity> activities =
                stashManager.getPullRequestActivities( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1, 1 );

        assertFalse( activities.isEmpty() );
    }


    @Test
    public void testGetPrCommits() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUEST_COMMITS_JSON );

        Set<Commit> commits = stashManager.getPullRequestCommits( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1, 1 );

        assertFalse( commits.isEmpty() );
    }


    @Test
    public void testBranches() throws Exception
    {
        setRestResponse( TestUtil.STASH_BRANCHES_JSON );

        Set<Branch> branches = stashManager.getBranches( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 1 );

        assertFalse( branches.isEmpty() );
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

        Set<Change> changes = stashManager.getPullRequestChanges( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 21, 10 );

        assertFalse( changes.isEmpty() );
    }


    @Test
    public void testGetChangesBetweenCommits() throws Exception
    {
        setRestResponse( TestUtil.STASH_PULL_REQUEST_CHANGES_JSON );

        Set<Change> changes = stashManager
                .getChangesBetweenCommits( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, "683ade1", "3353762", 10 );

        assertFalse( changes.isEmpty() );
    }


    @Test
    public void testGetCommits() throws Exception
    {

        setRestResponse( TestUtil.STASH_COMMITS_JSON );


        Set<Commit> commits = stashManager.getCommits( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 10 );

        assertFalse( commits.isEmpty() );
    }


    @Test
    public void testGetCommit() throws Exception
    {
        setRestResponse( TestUtil.STASH_COMMIT_JSON );

        Commit commit = stashManager.getCommit( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, "ad31de9" );

        assertNotNull( commit );

        System.out.println( commit );
    }


    @Test
    public void testGetCommitChanges() throws Exception
    {
        setRestResponse( TestUtil.STASH_COMMIT_CHANGES_JSON );

        Set<Change> changes = stashManager.getCommitChanges( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, "2fda08f", 10 );

        assertFalse( changes.isEmpty() );
    }


    @Test
    public void testGetProjectEvents() throws Exception
    {
        setRestResponse( TestUtil.STASH_EVENTS_JSON );

        Set<Event> events = stashManager.getProjectEvents( TestUtil.PROJECT_KEY, 10 );

        assertFalse( events.isEmpty() );
    }


    @Test
    public void testGetRepoEvents() throws Exception
    {
        setRestResponse( TestUtil.STASH_EVENTS_JSON );


        Set<Event> events = stashManager.getRepoEvents( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG, 10 );

        assertFalse( events.isEmpty() );
    }


    @Test
    public void testGetCommitBuildStatistics() throws Exception
    {
        setRestResponse( TestUtil.STASH_BUILD_STATISTICS_JSON );

        BuildStatistics buildStats = stashManager.getCommitBuildStatistics( "2fda08f" );

        assertNotNull( buildStats );
    }


    @Test
    public void testGetCommitBuildStatuses() throws Exception
    {

        setRestResponse( TestUtil.STASH_BUILD_STATUSES_JSON );

        Set<BuildStatus> buildStatuses = stashManager.getCommitBuildStatuses( "2fda08f", 10 );

        assertFalse( buildStatuses.isEmpty() );
    }
}
