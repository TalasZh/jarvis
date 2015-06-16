package org.safehaus.sonar.client;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.sonar.model.ComplexityStats;
import org.safehaus.sonar.model.DuplicationStats;
import org.safehaus.sonar.model.QuantitativeStats;
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Query;
import org.sonar.wsclient.services.Resource;

import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;


@RunWith( MockitoJUnitRunner.class )
public class SonarManagerImplTest
{
    private static final String RESOURCE_ID = "org.safehaus.subutai:mgmt-parent";
    private static final double MEASURE_VALUE = 123;
    @Mock
    Sonar sonarClient;
    @Mock
    Resource resource;
    @Mock
    Measure measure;

    SonarManagerImpl sonarManager;


    @Before
    public void setUp() throws Exception
    {
        sonarManager = new SonarManagerImpl( "http://sonar.subutai.io", "dilshat.aliev", "sadilya" );
        doReturn( resource ).when( sonarClient ).find( any( Query.class ) );
        doReturn( measure ).when( resource ).getMeasure( anyString() );
        doReturn( MEASURE_VALUE ).when( measure ).getValue();

        sonarManager.sonarClient = sonarClient;
    }


    @Test
    public void testGetUnitTestStats() throws Exception
    {
        UnitTestStats unitTestStats = sonarManager.getUnitTestStats( RESOURCE_ID );

        assertNotNull( unitTestStats );
    }


    @Test
    public void testGetViolationStats() throws Exception
    {
        ViolationStats violationStats = sonarManager.getViolationStats( RESOURCE_ID );

        assertNotNull( violationStats );
    }


    @Test
    public void testGetComplexityStats() throws Exception
    {
        ComplexityStats complexityStats = sonarManager.getComplexityStats( RESOURCE_ID );

        assertNotNull( complexityStats );
    }


    @Test
    public void testGetDuplicationStats() throws Exception
    {
        DuplicationStats duplicationStats = sonarManager.getDuplicationStats( RESOURCE_ID );

        assertNotNull( duplicationStats );
    }


    @Test
    public void testGetQuantitativeStats() throws Exception
    {
        QuantitativeStats quantitativeStats = sonarManager.getQuantitativeStats( RESOURCE_ID );

        assertNotNull( quantitativeStats );
    }
}
