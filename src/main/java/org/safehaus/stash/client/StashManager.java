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
import org.safehaus.stash.model.Project;
import org.safehaus.stash.model.PullRequest;
import org.safehaus.stash.model.Repo;
import org.safehaus.stash.util.RestUtil;


public interface StashManager
{
    public Set<Project> getProjects( int limit ) throws RestUtil.RestException;

    public Project getProject( String projectKey ) throws RestUtil.RestException;

    public Set<Group> getPermittedGroups( String projectKey, int limit ) throws RestUtil.RestException;

    public Set<Repo> getRepos( String projectKey, int limit ) throws RestUtil.RestException;

    public Repo getRepo( String projectKey, String repoSlug ) throws RestUtil.RestException;

    public Set<PullRequest> getPullRequests( String projectKey, String repoSlug, String branchName,
                                             PullRequest.State state, int limit ) throws RestUtil.RestException;

    public PullRequest getPullRequest( String projectKey, String repoSlug, long prId ) throws RestUtil.RestException;

    public Set<Activity> getPullRequestActivities( String projectKey, String repoSlug, long prId, int limit )
            throws RestUtil.RestException;

    public Set<Commit> getPullRequestCommits( String projectKey, String repoSlug, long prId, int limit )
            throws RestUtil.RestException;

    public Set<Change> getPullRequestChanges( String projectKey, String repoSlug, long prId, int limit )
            throws RestUtil.RestException;


    public Set<Branch> getBranches( String projectKey, String repoSlug, int limit ) throws RestUtil.RestException;

    public Branch getDefaultBranch( String projectKey, String repoSlug ) throws RestUtil.RestException;

    public Set<Change> getChangesBetweenCommits( String projectKey, String repoSlug, String fromCommitId,
                                                 String toCommitId, int limit ) throws RestUtil.RestException;

    public Set<Commit> getCommits( String projectKey, String repoSlug, int limit ) throws RestUtil.RestException;

    public Commit getCommit( String projectKey, String repoSlug, String commitId ) throws RestUtil.RestException;

    public Set<Change> getCommitChanges( String projectKey, String repoSlug, String commitId, int limit )
            throws RestUtil.RestException;

    public Set<Event> getRepoEvents( String projectKey, String repoSlug, int limit ) throws RestUtil.RestException;

    public Set<Event> getProjectEvents( String projectKey, int limit ) throws RestUtil.RestException;

    public BuildStatistics getCommitBuildStatistics( String commitId ) throws RestUtil.RestException;

    public Set<BuildStatus> getCommitBuildStatuses( String commitId, int limit ) throws RestUtil.RestException;

    public Set<JiraIssue> getJiraIssuesByPullRequest( String projectKey, String repoSlug, long prId )
            throws RestUtil.RestException;
}
