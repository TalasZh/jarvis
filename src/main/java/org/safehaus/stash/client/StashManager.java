package org.safehaus.stash.client;


import java.util.Set;

import org.safehaus.stash.model.Activity;
import org.safehaus.stash.model.Branch;
import org.safehaus.stash.model.Commit;
import org.safehaus.stash.model.Group;
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


    public Set<Branch> getBranches( String projectKey, String repoSlug, int limit ) throws RestUtil.RestException;

    public Branch getDefaultBranch( String projectKey, String repoSlug ) throws RestUtil.RestException;
}
