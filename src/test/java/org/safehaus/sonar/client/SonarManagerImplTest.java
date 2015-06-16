package org.safehaus.sonar.client;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.sonar.model.UnitTestStats;
import org.sonar.wsclient.Sonar;

import static junit.framework.TestCase.assertNotNull;


@RunWith( MockitoJUnitRunner.class )
public class SonarManagerImplTest
{
    private static  final String RESOURCE_ID = "org.safehaus.subutai:mgmt-parent";
    @Mock
    Sonar sonarClient;

    SonarManagerImpl sonarManager;


    @Before
    public void setUp() throws Exception
    {
        sonarManager = new SonarManagerImpl( "http://sonar.subutai.io", "dilshat.aliev", "sadilya" );
//        sonarManager.sonarClient = sonarClient;
    }


    @Test
    public void testGetUnitTestStats() throws Exception
    {
        UnitTestStats unitTestStats = sonarManager.getUnitTestStats( RESOURCE_ID );

        assertNotNull(unitTestStats);

        System.out.println(unitTestStats);

    }
}
