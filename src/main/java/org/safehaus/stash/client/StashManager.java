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


/**
 * This manager provide means to execute a most commonly used subset of Stash API. All methods are read-only, they do
 * not perform amy mutator operations on Stash
 *
 * Im most methods <b>limit</b> and <b>start</b> parameters are passed. They are used for setting paging configuration
 * of the results returned from Stash REST endpoints. See <a href="https://developer.atlassian.com/static/rest/stash/3.10.0/stash-rest.html#paging-params">Stash Paged Api</a>
 */
public interface StashManager
{
    public Page<Project> getProjects( int limit, int start ) throws StashManagerException;

    public Project getProject( String projectKey ) throws StashManagerException;

    public Page<Group> getPermittedGroups( String projectKey, int limit, int start ) throws StashManagerException;

    public Page<Repository> getRepos( String projectKey, int limit, int start ) throws StashManagerException;

    public Repository getRepo( String projectKey, String repoSlug ) throws StashManagerException;

    public Page<PullRequest> getPullRequests( String projectKey, String repoSlug, String branchName,
                                              PullRequestState state, int limit, int start )
            throws StashManagerException;

    public PullRequest getPullRequest( String projectKey, String repoSlug, long prId ) throws StashManagerException;

    public Page<Activity> getPullRequestActivities( String projectKey, String repoSlug, long prId, int limit,
                                                    int start ) throws StashManagerException;

    public Page<Commit> getPullRequestCommits( String projectKey, String repoSlug, long prId, int limit, int start )
            throws StashManagerException;

    public Page<Change> getPullRequestChanges( String projectKey, String repoSlug, long prId, int limit, int start )
            throws StashManagerException;


    public Page<Branch> getBranches( String projectKey, String repoSlug, int limit, int start )
            throws StashManagerException;

    public Branch getDefaultBranch( String projectKey, String repoSlug ) throws StashManagerException;

    public Page<Change> getChangesBetweenCommits( String projectKey, String repoSlug, String fromCommitId,
                                                  String toCommitId, int limit, int start )
            throws StashManagerException;

    public Page<Commit> getCommits( String projectKey, String repoSlug, int limit, int start )
            throws StashManagerException;

    public Commit getCommit( String projectKey, String repoSlug, String commitId ) throws StashManagerException;

    public Page<Change> getCommitChanges( String projectKey, String repoSlug, String commitId, int limit, int start )
            throws StashManagerException;

    public Page<Event> getRepoEvents( String projectKey, String repoSlug, int limit, int start )
            throws StashManagerException;

    public Page<Event> getProjectEvents( String projectKey, int limit, int start ) throws StashManagerException;

    public BuildStats getCommitBuildStatistics( String commitId ) throws StashManagerException;

    public Page<BuildStatus> getCommitBuildStatuses( String commitId, int limit, int start )
            throws StashManagerException;

    public Set<JiraIssue> getJiraIssuesByPullRequest( String projectKey, String repoSlug, long prId )
            throws StashManagerException;

    public Page<ChangeSet> getChangesByJiraIssue( String issueKey, int limit, int start, int maxChanges )
            throws StashManagerException;
}
