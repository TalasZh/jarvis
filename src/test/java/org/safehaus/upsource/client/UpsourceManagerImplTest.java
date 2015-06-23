package org.safehaus.upsource.client;


import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.upsource.model.Project;
import org.safehaus.upsource.model.Revision;
import org.safehaus.upsource.util.TestUtil;
import org.safehaus.util.RestUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;


@RunWith( MockitoJUnitRunner.class )
public class UpsourceManagerImplTest
{

    @Mock
    RestUtil restUtil;

    UpsourceManagerImpl upsourceManager;


    @Before
    public void setUp() throws Exception
    {
        upsourceManager = new UpsourceManagerImpl( "http://upsource.subutai.io", "upsource-bot", "Uy/4eN]7+~}h8tUG" );
    }


    private void setResponse( String response ) throws RestUtil.RestException
    {
        upsourceManager.restUtil = restUtil;
        doReturn( response ).when( restUtil ).get( anyString(), anyMap() );
    }


    @Test
    public void testGetAllProjects() throws Exception
    {
        setResponse( TestUtil.PROJECTS_JSON );

        Set<Project> projects = upsourceManager.getAllProjects();

        assertFalse( projects.isEmpty() );
    }


    @Test
    public void testGetProject() throws Exception
    {
        setResponse( TestUtil.PROJECT_JSON );

        Project project = upsourceManager.getProject( TestUtil.PROJECT_ID );

        assertNotNull( project );
    }


    @Test
    public void testGetRevisions() throws Exception
    {
        setResponse( TestUtil.REVISIONS_JSON );

        Set<Revision> revisions = upsourceManager.getRevisions( TestUtil.PROJECT_ID, 10 );

        assertFalse( revisions.isEmpty() );
    }


    @Test
    public void testGetHeadRevision() throws Exception
    {
        setResponse( TestUtil.REVISION_JSON );

        Revision revision = upsourceManager.getHeadRevision( TestUtil.PROJECT_ID );

        assertNotNull( revision );
    }


    @Test
    public void testGetRevision() throws Exception
    {
        setResponse( TestUtil.REVISION_JSON );

        Revision revision = upsourceManager.getRevision( TestUtil.PROJECT_ID, TestUtil.REVISION_ID );

        assertNotNull( revision );
    }


    @Test
    public void testGetFilteredRevisions() throws Exception
    {
        setResponse( TestUtil.REVISIONS_JSON );

        Set<Revision> revisions =
                upsourceManager.getFilteredRevisions( TestUtil.PROJECT_ID, 10, TestUtil.REVISION_FILTER );

        assertFalse( revisions.isEmpty() );
    }
}
