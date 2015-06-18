package org.safehaus.sonar.client;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.sonar.model.ComplexityStats;
import org.safehaus.sonar.model.DuplicationStats;
import org.safehaus.sonar.model.QuantitativeStats;
import org.safehaus.sonar.model.TimeComplexityStats;
import org.safehaus.sonar.model.TimeDuplicationStats;
import org.safehaus.sonar.model.TimeUnitTestStats;
import org.safehaus.sonar.model.TimeViolationStats;
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;
import org.sonar.wsclient.services.TimeMachine;
import org.sonar.wsclient.services.TimeMachineCell;
import org.sonar.wsclient.services.TimeMachineQuery;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;


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
    @Mock
    TimeMachine timeMachine;
    @Mock
    TimeMachineCell timeMachineCell;

    SonarManagerImpl sonarManager;


    @Before
    public void setUp() throws Exception
    {
        sonarManager = spy( new SonarManagerImpl( "http://sonar.subutai.io", "username", "password" ) );
        doReturn( resource ).when( sonarClient ).find( isA( ResourceQuery.class ) );
        doReturn( timeMachine ).when( sonarClient ).find( isA( TimeMachineQuery.class ) );
        doReturn( measure ).when( resource ).getMeasure( anyString() );
        doReturn( MEASURE_VALUE ).when( measure ).getValue();
        TimeMachineCell[] cells = new TimeMachineCell[] { timeMachineCell };
        doReturn( cells ).when( timeMachine ).getCells();

        sonarManager.sonarClient = sonarClient;
        doReturn( 123d ).when( sonarManager )
                        .getTimeValue( anyString(), any( TimeMachine.class ), any( TimeMachineCell.class ) );
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


    private static Date date( String date ) throws ParseException
    {
        return new SimpleDateFormat( "yyyy-MM-dd" ).parse( date );
    }


    @Test
    public void testGetTimeUnitTestStats() throws Exception
    {
        Set<TimeUnitTestStats> timeUnitTestStats =
                sonarManager.getTimeUnitTestStats( RESOURCE_ID, date( "2015-01-01" ), new Date() );

        assertFalse( timeUnitTestStats.isEmpty() );
    }


    @Test
    public void testGetTimeViolationStats() throws Exception
    {
        Set<TimeViolationStats> timeViolationStats =
                sonarManager.getTimeViolationStats( RESOURCE_ID, date( "2015-01-01" ), new Date() );

        assertFalse( timeViolationStats.isEmpty() );
    }


    @Test
    public void testGetTimeComplexityStats() throws Exception
    {
        Set<TimeComplexityStats> timeComplexityStats =
                sonarManager.getTimeComplexityStats( RESOURCE_ID, date( "2015-01-01" ), new Date() );

        assertFalse( timeComplexityStats.isEmpty() );
    }


    @Test
    public void testGetTimeDuplicationStats() throws Exception
    {
        Set<TimeDuplicationStats> timeDuplicationStats =
                sonarManager.getTimeDuplicationStats( RESOURCE_ID, date( "2015-01-01" ), new Date() );

        assertFalse( timeDuplicationStats.isEmpty() );


        for ( TimeDuplicationStats stats : timeDuplicationStats )
        {
            System.out.println( stats );
        }
    }
}
