package org.safehaus.stash.client;


import java.util.Set;

import org.safehaus.stash.model.Activity;
import org.safehaus.stash.model.Branch;
import org.safehaus.stash.model.BuildStatistics;
import org.safehaus.stash.model.BuildStatus;
import org.safehaus.stash.model.Change;
import org.safehaus.stash.model.Commit;
import org.safehaus.stash.model.Event;
import org.safehaus.stash.model.Group;
import org.safehaus.stash.model.JiraIssue;
import org.safehaus.stash.model.JiraIssueChange;
import org.safehaus.stash.model.Project;
import org.safehaus.stash.model.PullRequest;
import org.safehaus.stash.model.Repo;
import org.safehaus.stash.util.JsonUtil;
import org.safehaus.stash.util.RestUtil;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;


public class StashManagerImpl implements StashManager
{
    private final String baseUrl;

    protected RestUtil restUtil = new RestUtil();
    protected JsonUtil jsonUtil = new JsonUtil();

    //TODO wrap json cast into try-catch and throw StashManagerException
    //TODO use paging mechanism of Atlassian


    public StashManagerImpl( final String baseUrl )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );

        this.baseUrl = baseUrl;
    }


    protected String formUrl( String apiPath, Object... args )
    {
        return String.format( "%s/%s", baseUrl, String.format( apiPath, args ) );
    }


    @Override
    public Set<Project> getProjects( int limit ) throws RestUtil.RestException
    {
        String response =
                restUtil.get( formUrl( "rest/api/1.0/projects?limit=%d", limit ), Maps.<String, String>newHashMap() );


        Page<Project> projectPage = jsonUtil.from( response, new TypeToken<Page<Project>>()
        {}.getType() );

        return projectPage.getValues();
    }


    @Override
    public Project getProject( final String projectKey ) throws RestUtil.RestException
    {
        String response =
                restUtil.get( formUrl( "rest/api/1.0/projects/%s", projectKey ), Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, Project.class );
    }


    @Override
    public Set<Group> getPermittedGroups( final String projectKey, int limit ) throws RestUtil.RestException
    {
        String response =
                restUtil.get( formUrl( "rest/api/1.0/projects/%s/permissions/groups?limit=%d", projectKey, limit ),
                        Maps.<String, String>newHashMap() );

        Page<Group> groupPage = jsonUtil.from( response, new TypeToken<Page<Group>>()
        {}.getType() );

        return groupPage.getValues();
    }


    @Override
    public Set<Repo> getRepos( final String projectKey, int limit ) throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/api/1.0/projects/%s/repos?limit=%d", projectKey, limit ),
                Maps.<String, String>newHashMap() );

        Page<Repo> repoPage = jsonUtil.from( response, new TypeToken<Page<Repo>>()
        {}.getType() );

        return repoPage.getValues();
    }


    @Override
    public Repo getRepo( final String projectKey, final String repoSlug ) throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/api/1.0/projects/%s/repos/%s", projectKey, repoSlug ),
                Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, Repo.class );
    }


    @Override
    public Set<PullRequest> getPullRequests( final String projectKey, final String repoSlug, final String branchName,
                                             final PullRequest.State state, int limit ) throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests?state=%s&at=refs/heads/%s&limit=%d",
                        projectKey, repoSlug, state.name(), branchName, limit ), Maps.<String, String>newHashMap() );

        Page<PullRequest> pullRequestPage = jsonUtil.from( response, new TypeToken<Page<PullRequest>>()
        {}.getType() );

        return pullRequestPage.getValues();
    }


    @Override
    public PullRequest getPullRequest( final String projectKey, final String repoSlug, final long prId )
            throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d", projectKey, repoSlug, prId ),
                Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, PullRequest.class );
    }


    @Override
    public Set<Activity> getPullRequestActivities( final String projectKey, final String repoSlug, final long prId,
                                                   int limit ) throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/activities?limit=%d", projectKey, repoSlug,
                        prId, limit ), Maps.<String, String>newHashMap() );

        Page<Activity> activityPage = jsonUtil.from( response, new TypeToken<Page<Activity>>()
        {}.getType() );

        return activityPage.getValues();
    }


    @Override
    public Set<Commit> getPullRequestCommits( final String projectKey, final String repoSlug, final long prId,
                                              final int limit ) throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/commits?limit=%d", projectKey, repoSlug,
                        prId, limit ), Maps.<String, String>newHashMap() );

        Page<Commit> commitPage = jsonUtil.from( response, new TypeToken<Page<Commit>>()
        {}.getType() );

        return commitPage.getValues();
    }


    @Override
    public Set<Change> getPullRequestChanges( final String projectKey, final String repoSlug, final long prId,
                                              final int limit ) throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/changes?limit=%d", projectKey, repoSlug,
                        prId, limit ), Maps.<String, String>newHashMap() );

        Page<Change> commitPage = jsonUtil.from( response, new TypeToken<Page<Change>>()
        {}.getType() );

        return commitPage.getValues();
    }


    @Override
    public Set<Branch> getBranches( final String projectKey, final String repoSlug, final int limit )
            throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/branches?limit=%d", projectKey, repoSlug, limit ),
                Maps.<String, String>newHashMap() );

        Page<Branch> branchPage = jsonUtil.from( response, new TypeToken<Page<Branch>>()
        {}.getType() );

        return branchPage.getValues();
    }


    @Override
    public Branch getDefaultBranch( final String projectKey, final String repoSlug ) throws RestUtil.RestException
    {
        String response =
                restUtil.get( formUrl( "rest/api/1.0/projects/%s/repos/%s/branches/default", projectKey, repoSlug ),
                        Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, Branch.class );
    }


    @Override
    public Set<Change> getChangesBetweenCommits( final String projectKey, final String repoSlug,
                                                 final String fromCommitId, final String toCommitId, final int limit )
            throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/changes?since=%s&until=%s&limit=%d", projectKey, repoSlug,
                        fromCommitId, toCommitId, limit ), Maps.<String, String>newHashMap() );

        Page<Change> commitPage = jsonUtil.from( response, new TypeToken<Page<Change>>()
        {}.getType() );

        return commitPage.getValues();
    }


    @Override
    public Set<Commit> getCommits( final String projectKey, final String repoSlug, final int limit )
            throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/commits?limit=%d", projectKey, repoSlug, limit ),
                Maps.<String, String>newHashMap() );

        Page<Commit> commitPage = jsonUtil.from( response, new TypeToken<Page<Commit>>()
        {}.getType() );

        return commitPage.getValues();
    }


    @Override
    public Commit getCommit( final String projectKey, final String repoSlug, final String commitId )
            throws RestUtil.RestException
    {
        String response =
                restUtil.get( formUrl( "rest/api/1.0/projects/%s/repos/%s/commits/%s", projectKey, repoSlug, commitId ),
                        Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, Commit.class );
    }


    @Override
    public Set<Change> getCommitChanges( final String projectKey, final String repoSlug, final String commitId,
                                         final int limit ) throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/api/1.0/projects/%s/repos/%s/commits/%s/changes?limit=%d", projectKey, repoSlug,
                        commitId, limit ), Maps.<String, String>newHashMap() );

        Page<Change> changePage = jsonUtil.from( response, new TypeToken<Page<Change>>()
        {}.getType() );

        return changePage.getValues();
    }


    @Override
    public Set<Event> getProjectEvents( final String projectKey, final int limit ) throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/audit/1.0/projects/%s/events?limit=%d", projectKey, limit ),
                Maps.<String, String>newHashMap() );

        Page<Event> eventPage = jsonUtil.from( response, new TypeToken<Page<Event>>()
        {}.getType() );

        return eventPage.getValues();
    }


    @Override
    public Set<Event> getRepoEvents( final String projectKey, final String repoSlug, final int limit )
            throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/audit/1.0/projects/%s/repos/%s/events?limit=%d", projectKey, repoSlug, limit ),
                Maps.<String, String>newHashMap() );

        Page<Event> eventPage = jsonUtil.from( response, new TypeToken<Page<Event>>()
        {}.getType() );

        return eventPage.getValues();
    }


    @Override
    public BuildStatistics getCommitBuildStatistics( final String commitId ) throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/build-status/1.0/commits/stats/%s", commitId ),
                Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, BuildStatistics.class );
    }


    @Override
    public Set<BuildStatus> getCommitBuildStatuses( final String commitId, final int limit )
            throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/build-status/1.0/commits/%s?limit=%d", commitId, limit ),
                Maps.<String, String>newHashMap() );

        Page<BuildStatus> buildStatusPage = jsonUtil.from( response, new TypeToken<Page<BuildStatus>>()
        {}.getType() );

        return buildStatusPage.getValues();
    }


    @Override
    public Set<JiraIssue> getJiraIssuesByPullRequest( final String projectKey, final String repoSlug, final long prId )
            throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/jira/1.0/projects/%s/repos/%s/pull-requests/%d/issues", projectKey, repoSlug, prId ),
                Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, new TypeToken<Set<JiraIssue>>()
        {}.getType() );
    }


    @Override
    public Page<JiraIssueChange> getChangesByJiraIssue( final String issueKey, final int limit, final int start,
                                                        final int maxChanges ) throws RestUtil.RestException
    {
        String response = restUtil.get(
                formUrl( "rest/jira/1.0/issues/%s/commits?limit=%d&start=%d&maxChanges=%d", issueKey, limit, start,
                        maxChanges ), Maps.<String, String>newHashMap() );

        return jsonUtil.from( response, new TypeToken<Page<JiraIssueChange>>()
        {}.getType() );
    }
}
