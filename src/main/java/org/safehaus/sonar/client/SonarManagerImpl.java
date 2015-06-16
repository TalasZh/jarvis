package org.safehaus.sonar.client;


import org.safehaus.sonar.model.UnitTestStats;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;


public class SonarManagerImpl implements SonarManager
{
    protected Sonar sonarClient;


    public SonarManagerImpl( final String baseUrl, final String username, final String password )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );
        Preconditions.checkArgument( !Strings.isNullOrEmpty( username ) );
        Preconditions.checkArgument( !Strings.isNullOrEmpty( password ) );

        this.sonarClient = Sonar.create( baseUrl, username, password );
    }


    @Override
    public UnitTestStats getUnitTestStats( final String resourceId ) throws SonarManagerException
    {
        try
        {
            Resource resource = sonarClient.find( ResourceQuery
                    .createForMetrics( resourceId, UnitTestStats.SUCCESS_PERCENT_METRIC, UnitTestStats.FAILURES_METRIC,
                            UnitTestStats.ERRORS_METRIC, UnitTestStats.TESTS_COUNT_METRIC,
                            UnitTestStats.EXEC_TIME_METRIC, UnitTestStats.COVERAGE_METRIC,
                            UnitTestStats.LINE_COVERAGE_METRIC, UnitTestStats.BRANCH_COVERAGE_METRIC ) );


            return new UnitTestStats( resource.getMeasure( UnitTestStats.SUCCESS_PERCENT_METRIC ).getValue(),
                    resource.getMeasure( UnitTestStats.FAILURES_METRIC ).getValue(),
                    resource.getMeasure( UnitTestStats.ERRORS_METRIC ).getValue(),
                    resource.getMeasure( UnitTestStats.TESTS_COUNT_METRIC ).getValue(),
                    resource.getMeasure( UnitTestStats.EXEC_TIME_METRIC ).getValue(),
                    resource.getMeasure( UnitTestStats.COVERAGE_METRIC ).getValue(),
                    resource.getMeasure( UnitTestStats.LINE_COVERAGE_METRIC ).getValue(),
                    resource.getMeasure( UnitTestStats.BRANCH_COVERAGE_METRIC ).getValue() );
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }
    }
}
