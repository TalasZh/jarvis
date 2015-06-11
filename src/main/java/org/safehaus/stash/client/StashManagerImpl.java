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

    //TODO try to use Atlassian native objects


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
    public Page<Project> getProjects( final int limit, final int start ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get( formUrl( "rest/api/1.0/projects?limit=%d&start=%d", limit, start ),
                    Maps.<String, String>newHashMap() );


            return jsonUtil.from( response, new TypeToken<Page<Project>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Project getProject( final String projectKey ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get( formUrl( "rest/api/1.0/projects/%s", projectKey ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, Project.class );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Group> getPermittedGroups( final String projectKey, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/permissions/groups?limit=%d&start=%d", projectKey, limit,
                            start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Group>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Repo> getRepos( final String projectKey, final int limit, final int start ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos?limit=%d&start=%d", projectKey, limit, start ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Repo>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Repo getRepo( final String projectKey, final String repoSlug ) throws StashManagerException
    {
        try
        {

            String response = restUtil.get( formUrl( "rest/api/1.0/projects/%s/repos/%s", projectKey, repoSlug ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, Repo.class );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<PullRequest> getPullRequests( final String projectKey, final String repoSlug, final String branchName,
                                              final PullRequest.State state, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get( formUrl(
                            "rest/api/1.0/projects/%s/repos/%s/pull-requests?state=%s&at=refs/heads/%s&limit=%d&start"
                                    + "=%d", projectKey, repoSlug, state.name(), branchName, limit, start ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<PullRequest>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public PullRequest getPullRequest( final String projectKey, final String repoSlug, final long prId )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d", projectKey, repoSlug, prId ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, PullRequest.class );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Activity> getPullRequestActivities( final String projectKey, final String repoSlug, final long prId,
                                                    final int limit, final int start ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/activities?limit=%d&start=%d",
                            projectKey, repoSlug, prId, limit, start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Activity>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Commit> getPullRequestCommits( final String projectKey, final String repoSlug, final long prId,
                                               final int limit, final int start ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/commits?limit=%d&start=%d", projectKey,
                            repoSlug, prId, limit, start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Commit>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Change> getPullRequestChanges( final String projectKey, final String repoSlug, final long prId,
                                               final int limit, final int start ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/changes?limit=%d&start=%d", projectKey,
                            repoSlug, prId, limit, start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Change>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Branch> getBranches( final String projectKey, final String repoSlug, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/branches?limit=%d&start=%d", projectKey, repoSlug,
                            limit, start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Branch>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Branch getDefaultBranch( final String projectKey, final String repoSlug ) throws StashManagerException
    {
        try
        {
            String response =
                    restUtil.get( formUrl( "rest/api/1.0/projects/%s/repos/%s/branches/default", projectKey, repoSlug ),
                            Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, Branch.class );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Change> getChangesBetweenCommits( final String projectKey, final String repoSlug,
                                                  final String fromCommitId, final String toCommitId, final int limit,
                                                  final int start ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/changes?since=%s&until=%s&limit=%d&start=%d",
                            projectKey, repoSlug, fromCommitId, toCommitId, limit, start ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Change>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Commit> getCommits( final String projectKey, final String repoSlug, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/commits?limit=%d&start=%d", projectKey, repoSlug, limit,
                            start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Commit>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Commit getCommit( final String projectKey, final String repoSlug, final String commitId )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/commits/%s", projectKey, repoSlug, commitId ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, Commit.class );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Change> getCommitChanges( final String projectKey, final String repoSlug, final String commitId,
                                          final int limit, final int start ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/api/1.0/projects/%s/repos/%s/commits/%s/changes?limit=%d&start=%d", projectKey,
                            repoSlug, commitId, limit, start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Change>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Event> getProjectEvents( final String projectKey, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/audit/1.0/projects/%s/events?limit=%d&start=%d", projectKey, limit, start ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Event>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Event> getRepoEvents( final String projectKey, final String repoSlug, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/audit/1.0/projects/%s/repos/%s/events?limit=%d&start=%d", projectKey, repoSlug,
                            limit, start ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<Event>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public BuildStatistics getCommitBuildStatistics( final String commitId ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get( formUrl( "rest/build-status/1.0/commits/stats/%s", commitId ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, BuildStatistics.class );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<BuildStatus> getCommitBuildStatuses( final String commitId, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/build-status/1.0/commits/%s?limit=%d&start=%d", commitId, limit, start ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<BuildStatus>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Set<JiraIssue> getJiraIssuesByPullRequest( final String projectKey, final String repoSlug, final long prId )
            throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/jira/1.0/projects/%s/repos/%s/pull-requests/%d/issues", projectKey, repoSlug, prId ),
                    Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Set<JiraIssue>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<JiraIssueChange> getChangesByJiraIssue( final String issueKey, final int limit, final int start,
                                                        final int maxChanges ) throws StashManagerException
    {
        try
        {
            String response = restUtil.get(
                    formUrl( "rest/jira/1.0/issues/%s/commits?limit=%d&start=%d&maxChanges=%d", issueKey, limit, start,
                            maxChanges ), Maps.<String, String>newHashMap() );

            return jsonUtil.from( response, new TypeToken<Page<JiraIssueChange>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }
}
