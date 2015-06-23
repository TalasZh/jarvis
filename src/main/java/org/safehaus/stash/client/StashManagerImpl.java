package org.safehaus.stash.client;


import java.util.Set;

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
import org.safehaus.stash.util.JsonUtil;
import org.safehaus.util.JarvisContextHolder;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;


public class StashManagerImpl implements StashManager
{
    private final String baseUrl;

    private String username;
    private String password;

    protected JsonUtil jsonUtil = new JsonUtil();


    /**
     * This constructor is used when Stash is to be accessed in the context of a user's web session based on Crowd SSO cookie.
     * SSO cookie is retrieved from JarvisContext
     *
     * @param baseUrl - base url of Stash e.g. stash.my-company.com
     */
    public StashManagerImpl( final String baseUrl )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );

        this.baseUrl = baseUrl;
    }


    /**
     * This constructor is used when Stash is to be accessed using a user's credentials
     *
     * @param baseUrl - base url of Stash e.g. stash.my-company.com
     * @param username - username of user
     * @param password - password of user
     */
    public StashManagerImpl( final String baseUrl, final String username, final String password )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );
        Preconditions.checkArgument( !Strings.isNullOrEmpty( username ) );
        Preconditions.checkArgument( !Strings.isNullOrEmpty( password ) );

        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
    }


    protected String get( String apiPath, Object... args ) throws AtlassianRestUtil.RestException, StashManagerException
    {
        if ( !Strings.isNullOrEmpty( username ) && !Strings.isNullOrEmpty( password ) )
        {
            return new AtlassianRestUtil( username, password )
                    .get( String.format( "%s/%s", baseUrl, String.format( apiPath, args ) ), null );
        }
        else if ( JarvisContextHolder.getContext() != null && JarvisContextHolder.getContext().getCookie() != null )
        {
            return new AtlassianRestUtil( JarvisContextHolder.getContext().getCookie() )
                    .get( String.format( "%s/%s", baseUrl, String.format( apiPath, args ) ), null );
        }
        else
        {
            throw new StashManagerException( "No valid authorization info found" );
        }
    }


    @Override
    public Page<Project> getProjects( final int limit, final int start ) throws StashManagerException
    {
        try
        {
            String response = get( "rest/api/1.0/projects?limit=%d&start=%d", limit, start );

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
            String response = get( "rest/api/1.0/projects/%s", projectKey );

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
            String response =
                    get( "rest/api/1.0/projects/%s/permissions/groups?limit=%d&start=%d", projectKey, limit, start );

            return jsonUtil.from( response, new TypeToken<Page<Group>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<Repository> getRepos( final String projectKey, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response = get( "rest/api/1.0/projects/%s/repos?limit=%d&start=%d", projectKey, limit, start );

            return jsonUtil.from( response, new TypeToken<Page<Repository>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Repository getRepo( final String projectKey, final String repoSlug ) throws StashManagerException
    {
        try
        {

            String response = get( "rest/api/1.0/projects/%s/repos/%s", projectKey, repoSlug );

            return jsonUtil.from( response, Repository.class );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<PullRequest> getPullRequests( final String projectKey, final String repoSlug, final String branchName,
                                              final PullRequestState state, final int limit, final int start )
            throws StashManagerException
    {
        try
        {
            String response =
                    get( "rest/api/1.0/projects/%s/repos/%s/pull-requests?state=%s&at=refs/heads/%s&limit" + "=%d&start"
                            + "=%d", projectKey, repoSlug, state.name(), branchName, limit, start );

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
            String response = get( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d", projectKey, repoSlug, prId );

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
            String response = get( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/activities?limit=%d&start=%d" );

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
            String response =
                    get( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/commits?limit=%d&start=%d", projectKey,
                            repoSlug, prId, limit, start );

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
            String response =
                    get( "rest/api/1.0/projects/%s/repos/%s/pull-requests/%d/changes?limit=%d&start=%d", projectKey,
                            repoSlug, prId, limit, start );

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
            String response =
                    get( "rest/api/1.0/projects/%s/repos/%s/branches?limit=%d&start=%d", projectKey, repoSlug, limit,
                            start );

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
            String response = get( "rest/api/1.0/projects/%s/repos/%s/branches/default", projectKey, repoSlug );

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
            String response =
                    get( "rest/api/1.0/projects/%s/repos/%s/changes?since=%s&until=%s&limit=%d&start=%d", projectKey,
                            repoSlug, fromCommitId, toCommitId, limit, start );

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
            String response =
                    get( "rest/api/1.0/projects/%s/repos/%s/commits?limit=%d&start=%d", projectKey, repoSlug, limit,
                            start );

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
            String response = get( "rest/api/1.0/projects/%s/repos/%s/commits/%s", projectKey, repoSlug, commitId );

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
            String response =
                    get( "rest/api/1.0/projects/%s/repos/%s/commits/%s/changes?limit=%d&start=%d", projectKey, repoSlug,
                            commitId, limit, start );

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
            String response = get( "rest/audit/1.0/projects/%s/events?limit=%d&start=%d", projectKey, limit, start );

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
            String response =
                    get( "rest/audit/1.0/projects/%s/repos/%s/events?limit=%d&start=%d", projectKey, repoSlug, limit,
                            start );

            return jsonUtil.from( response, new TypeToken<Page<Event>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public BuildStats getCommitBuildStatistics( final String commitId ) throws StashManagerException
    {
        try
        {
            String response = get( "rest/build-status/1.0/commits/stats/%s", commitId );

            return jsonUtil.from( response, BuildStats.class );
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
            String response = get( "rest/build-status/1.0/commits/%s?limit=%d&start=%d", commitId, limit, start );

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
            String response =
                    get( "rest/jira/1.0/projects/%s/repos/%s/pull-requests/%d/issues", projectKey, repoSlug, prId );

            return jsonUtil.from( response, new TypeToken<Set<JiraIssue>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public Page<ChangeSet> getChangesByJiraIssue( final String issueKey, final int limit, final int start,
                                                  final int maxChanges ) throws StashManagerException
    {
        try
        {
            String response =
                    get( "rest/jira/1.0/issues/%s/commits?limit=%d&start=%d&maxChanges=%d", issueKey, limit, start,
                            maxChanges );

            return jsonUtil.from( response, new TypeToken<Page<ChangeSet>>()
            {}.getType() );
        }
        catch ( Exception e )
        {
            throw new StashManagerException( e );
        }
    }


    @Override
    public String getBaseUrl()
    {
        return baseUrl;
    }
}
