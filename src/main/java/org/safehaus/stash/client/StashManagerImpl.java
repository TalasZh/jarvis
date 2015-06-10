package org.safehaus.stash.client;


import java.util.Set;

import org.safehaus.stash.model.Group;
import org.safehaus.stash.model.Project;
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

    //TODO wrap json cast into try-catch and throw StashmanagerException

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


    protected String formUrl( String apiPath, String... args )
    {
        return String.format( "%s/%s", baseUrl, String.format( apiPath, args ) );
    }


    @Override
    public Set<Project> getProjects() throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/api/1.0/projects/" ), Maps.<String, String>newHashMap() );


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
    public Set<Group> getPermittedGroups( final String projectKey ) throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/api/1.0/projects/%s/permissions/groups", projectKey ),
                Maps.<String, String>newHashMap() );

        Page<Group> groupPage = jsonUtil.from( response, new TypeToken<Page<Group>>()
        {}.getType() );

        return groupPage.getValues();
    }


    @Override
    public Set<Repo> getRepos( final String projectKey ) throws RestUtil.RestException
    {
        String response = restUtil.get( formUrl( "rest/api/1.0/projects/%s/repos", projectKey ),
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
}
