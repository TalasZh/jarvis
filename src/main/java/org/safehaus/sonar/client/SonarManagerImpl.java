package org.safehaus.sonar.client;


import org.safehaus.sonar.model.ComplexityStats;
import org.safehaus.sonar.model.DuplicationStats;
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;
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


    @Override
    public ViolationStats getViolationStats( final String resourceId ) throws SonarManagerException
    {
        try
        {
            Resource resource = sonarClient.find( ResourceQuery
                    .createForMetrics( resourceId, ViolationStats.TECHNICAL_DEBT_METRIC,
                            ViolationStats.OPEN_ISSUES_METRIC, ViolationStats.REOPENED_ISSUES_METRIC,
                            ViolationStats.ALL_ISSUES_METRIC, ViolationStats.BLOCKER_ISSUES_METRIC,
                            ViolationStats.CRITICAL_ISSUES_METRIC, ViolationStats.MAJOR_ISSUES_METRIC,
                            ViolationStats.MINOR_ISSUES_METRIC, ViolationStats.INFO_ISSUES_METRIC ) );

            return new ViolationStats( resource.getMeasure( ViolationStats.TECHNICAL_DEBT_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.OPEN_ISSUES_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.REOPENED_ISSUES_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.ALL_ISSUES_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.BLOCKER_ISSUES_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.CRITICAL_ISSUES_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.MAJOR_ISSUES_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.MINOR_ISSUES_METRIC ).getValue(),
                    resource.getMeasure( ViolationStats.INFO_ISSUES_METRIC ).getValue() );
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }
    }


    @Override
    public ComplexityStats getComplexityStats( final String resourceId ) throws SonarManagerException
    {
        try
        {
            Resource resource = sonarClient.find( ResourceQuery
                    .createForMetrics( resourceId, ComplexityStats.COMPLEXITY_METRIC,
                            ComplexityStats.FILE_COMPLEXITY_METRIC, ComplexityStats.CLASS_COMPLEXITY_METRIC,
                            ComplexityStats.FUNCTION_COMPLEXITY_METRIC ) );

            return new ComplexityStats( resource.getMeasure( ComplexityStats.COMPLEXITY_METRIC ).getValue(),
                    resource.getMeasure( ComplexityStats.FILE_COMPLEXITY_METRIC ).getValue(),
                    resource.getMeasure( ComplexityStats.CLASS_COMPLEXITY_METRIC ).getValue(),
                    resource.getMeasure( ComplexityStats.FUNCTION_COMPLEXITY_METRIC ).getValue() );
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }
    }


    @Override
    public DuplicationStats getDuplicationStats( final String resourceId ) throws SonarManagerException
    {
        try
        {
            Resource resource = sonarClient.find( ResourceQuery
                    .createForMetrics( resourceId, DuplicationStats.DUPLICATION_PERCENT_METRIC,
                            DuplicationStats.DUPLICATED_LINES_METRIC, DuplicationStats.DUPLICATED_BLOCKS_METRIC,
                            DuplicationStats.DUPLICATED_FILES_METRIC ) );

            return new DuplicationStats( resource.getMeasure( DuplicationStats.DUPLICATION_PERCENT_METRIC ).getValue(),
                    resource.getMeasure( DuplicationStats.DUPLICATED_LINES_METRIC ).getValue(),
                    resource.getMeasure( DuplicationStats.DUPLICATED_BLOCKS_METRIC ).getValue(),
                    resource.getMeasure( DuplicationStats.DUPLICATED_FILES_METRIC ).getValue() );
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }
    }
}
