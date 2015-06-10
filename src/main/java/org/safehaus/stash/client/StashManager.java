package org.safehaus.stash.client;


import java.util.Set;

import org.safehaus.stash.model.Group;
import org.safehaus.stash.model.Project;
import org.safehaus.stash.model.Repo;
import org.safehaus.stash.util.RestUtil;


public interface StashManager
{
    public Set<Project> getProjects() throws RestUtil.RestException;

    public Project getProject( String projectKey ) throws RestUtil.RestException;

    public Set<Group> getPermittedGroups( String projectKey ) throws RestUtil.RestException;

    public Set<Repo> getRepos( String projectKey ) throws RestUtil.RestException;

    public Repo getRepo( String projectKey, String repoSlug ) throws RestUtil.RestException;
}
