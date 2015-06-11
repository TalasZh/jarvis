package org.safehaus.stash.client;


import java.util.Set;

import org.safehaus.stash.model.Activity;
import org.safehaus.stash.model.Branch;
import org.safehaus.stash.model.Change;
import org.safehaus.stash.model.Commit;
import org.safehaus.stash.model.Group;
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
    //TODO remove limit and set by default limit= Integer.MAX_VALUE


    public StashManagerImpl( final String baseUrl )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );

        this.baseUrl = baseUrl;
    }


    protected static class Page<T>
    {
        Set<T> values;


        public Set<T> getValues()
        {
            return values;
        }
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
}
