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
import org.safehaus.stash.model.Group;
import org.safehaus.stash.model.Project;
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

        Set<Project> projects = stashManager.getProjects();

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

        Set<Group> groups = stashManager.getPermittedGroups( TestUtil.PROJECT_KEY );

        assertTrue( groups.size() == 2 );
    }


    @Test
    public void testGetRepos() throws Exception
    {
        setRestResponse( TestUtil.STASH_REPOS_JSON );

        Set<Repo> repos = stashManager.getRepos( TestUtil.PROJECT_KEY );

        assertFalse( repos.isEmpty() );
    }


    @Test
    public void testGetRepo() throws Exception
    {
        setRestResponse( TestUtil.STASH_REPO_JSON );

        Repo repo = stashManager.getRepo( TestUtil.PROJECT_KEY, TestUtil.REPO_SLUG );

        assertNotNull( repo );
    }
}
