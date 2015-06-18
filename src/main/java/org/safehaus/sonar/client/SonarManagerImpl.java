package org.safehaus.sonar.client;


import java.util.Date;
import java.util.Set;

import org.safehaus.sonar.model.ComplexityStats;
import org.safehaus.sonar.model.DuplicationStats;
import org.safehaus.sonar.model.QuantitativeStats;
import org.safehaus.sonar.model.TimeComplexityStats;
import org.safehaus.sonar.model.TimeDuplicationStats;
import org.safehaus.sonar.model.TimeQuantitativeStats;
import org.safehaus.sonar.model.TimeUnitTestStats;
import org.safehaus.sonar.model.TimeViolationStats;
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;
import org.sonar.wsclient.services.TimeMachine;
import org.sonar.wsclient.services.TimeMachineCell;
import org.sonar.wsclient.services.TimeMachineColumn;
import org.sonar.wsclient.services.TimeMachineQuery;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;


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


    @Override
    public QuantitativeStats getQuantitativeStats( final String resourceId ) throws SonarManagerException
    {
        try
        {
            Resource resource = sonarClient.find( ResourceQuery
                    .createForMetrics( resourceId, QuantitativeStats.LINES_OF_CODE_METRIC,
                            QuantitativeStats.LINES_METRIC, QuantitativeStats.FILES_METRIC,
                            QuantitativeStats.DIRECTORIES_METRIC, QuantitativeStats.FUNCTIONS_METRIC,
                            QuantitativeStats.CLASSES_METRIC, QuantitativeStats.STATEMENTS_METRIC,
                            QuantitativeStats.ACCESSORS_METRIC ) );

            return new QuantitativeStats( resource.getMeasure( QuantitativeStats.LINES_OF_CODE_METRIC ).getValue(),
                    resource.getMeasure( QuantitativeStats.LINES_METRIC ).getValue(),
                    resource.getMeasure( QuantitativeStats.FILES_METRIC ).getValue(),
                    resource.getMeasure( QuantitativeStats.DIRECTORIES_METRIC ).getValue(),
                    resource.getMeasure( QuantitativeStats.FUNCTIONS_METRIC ).getValue(),
                    resource.getMeasure( QuantitativeStats.CLASSES_METRIC ).getValue(),
                    resource.getMeasure( QuantitativeStats.STATEMENTS_METRIC ).getValue(),
                    resource.getMeasure( QuantitativeStats.ACCESSORS_METRIC ).getValue() );
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }
    }


    protected double getTimeValue( String metric, TimeMachine timeMachine, TimeMachineCell cell )
    {
        for ( TimeMachineColumn column : timeMachine.getColumns() )
        {
            if ( column.getMetricKey().equals( metric ) )
            {
                Object value = cell.getValues()[column.getIndex()];
                return value instanceof Long ? ( ( Long ) value ).doubleValue() : ( double ) value;
            }
        }

        return -1d;
    }


    @Override
    public Set<TimeUnitTestStats> getTimeUnitTestStats( final String resourceId, final Date fromDate,
                                                        final Date toDate ) throws SonarManagerException
    {
        Set<TimeUnitTestStats> stats = Sets.newHashSet();
        try
        {
            TimeMachine timeMachine = sonarClient.find( TimeMachineQuery
                    .createForMetrics( resourceId, UnitTestStats.SUCCESS_PERCENT_METRIC, UnitTestStats.FAILURES_METRIC,
                            UnitTestStats.ERRORS_METRIC, UnitTestStats.TESTS_COUNT_METRIC,
                            UnitTestStats.EXEC_TIME_METRIC, UnitTestStats.COVERAGE_METRIC,
                            UnitTestStats.LINE_COVERAGE_METRIC, UnitTestStats.BRANCH_COVERAGE_METRIC )
                    .setFrom( fromDate ).setTo( toDate ) );

            for ( TimeMachineCell cell : timeMachine.getCells() )
            {
                stats.add(
                        new TimeUnitTestStats( getTimeValue( UnitTestStats.SUCCESS_PERCENT_METRIC, timeMachine, cell ),
                                getTimeValue( UnitTestStats.FAILURES_METRIC, timeMachine, cell ),
                                getTimeValue( UnitTestStats.ERRORS_METRIC, timeMachine, cell ),
                                getTimeValue( UnitTestStats.TESTS_COUNT_METRIC, timeMachine, cell ),
                                getTimeValue( UnitTestStats.EXEC_TIME_METRIC, timeMachine, cell ),
                                getTimeValue( UnitTestStats.COVERAGE_METRIC, timeMachine, cell ),
                                getTimeValue( UnitTestStats.LINE_COVERAGE_METRIC, timeMachine, cell ),
                                getTimeValue( UnitTestStats.BRANCH_COVERAGE_METRIC, timeMachine, cell ),
                                cell.getDate() ) );
            }


            return stats;
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }
    }


    @Override
    public Set<TimeViolationStats> getTimeViolationStats( final String resourceId, final Date fromDate,
                                                          final Date toDate ) throws SonarManagerException
    {
        Set<TimeViolationStats> stats = Sets.newHashSet();

        try
        {
            TimeMachine timeMachine = sonarClient.find( TimeMachineQuery
                    .createForMetrics( resourceId, ViolationStats.TECHNICAL_DEBT_METRIC,
                            ViolationStats.OPEN_ISSUES_METRIC, ViolationStats.REOPENED_ISSUES_METRIC,
                            ViolationStats.ALL_ISSUES_METRIC, ViolationStats.BLOCKER_ISSUES_METRIC,
                            ViolationStats.CRITICAL_ISSUES_METRIC, ViolationStats.MAJOR_ISSUES_METRIC,
                            ViolationStats.MINOR_ISSUES_METRIC, ViolationStats.INFO_ISSUES_METRIC ).setFrom( fromDate )
                    .setTo( toDate ) );


            for ( TimeMachineCell cell : timeMachine.getCells() )
            {
                stats.add(
                        new TimeViolationStats( getTimeValue( ViolationStats.TECHNICAL_DEBT_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.OPEN_ISSUES_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.REOPENED_ISSUES_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.ALL_ISSUES_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.BLOCKER_ISSUES_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.CRITICAL_ISSUES_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.MAJOR_ISSUES_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.MINOR_ISSUES_METRIC, timeMachine, cell ),
                                getTimeValue( ViolationStats.INFO_ISSUES_METRIC, timeMachine, cell ),
                                cell.getDate() ) );
            }
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }

        return stats;
    }


    @Override
    public Set<TimeComplexityStats> getTimeComplexityStats( final String resourceId, final Date fromDate,
                                                            final Date toDate ) throws SonarManagerException
    {
        Set<TimeComplexityStats> stats = Sets.newHashSet();

        try
        {
            TimeMachine timeMachine = sonarClient.find( TimeMachineQuery
                    .createForMetrics( resourceId, ComplexityStats.COMPLEXITY_METRIC,
                            ComplexityStats.FILE_COMPLEXITY_METRIC, ComplexityStats.CLASS_COMPLEXITY_METRIC,
                            ComplexityStats.FUNCTION_COMPLEXITY_METRIC ).setFrom( fromDate ).setTo( toDate ) );


            for ( TimeMachineCell cell : timeMachine.getCells() )
            {
                stats.add(
                        new TimeComplexityStats( getTimeValue( ComplexityStats.COMPLEXITY_METRIC, timeMachine, cell ),
                                getTimeValue( ComplexityStats.FILE_COMPLEXITY_METRIC, timeMachine, cell ),
                                getTimeValue( ComplexityStats.CLASS_COMPLEXITY_METRIC, timeMachine, cell ),
                                getTimeValue( ComplexityStats.FUNCTION_COMPLEXITY_METRIC, timeMachine, cell ),
                                cell.getDate() ) );
            }
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }

        return stats;
    }


    @Override
    public Set<TimeDuplicationStats> getTimeDuplicationStats( final String resourceId, final Date fromDate,
                                                              final Date toDate ) throws SonarManagerException
    {
        Set<TimeDuplicationStats> stats = Sets.newHashSet();

        try
        {
            TimeMachine timeMachine = sonarClient.find( TimeMachineQuery
                    .createForMetrics( resourceId, DuplicationStats.DUPLICATION_PERCENT_METRIC,
                            DuplicationStats.DUPLICATED_LINES_METRIC, DuplicationStats.DUPLICATED_BLOCKS_METRIC,
                            DuplicationStats.DUPLICATED_FILES_METRIC ).setFrom( fromDate ).setTo( toDate ) );

            for ( TimeMachineCell cell : timeMachine.getCells() )
            {
                stats.add( new TimeDuplicationStats(
                        getTimeValue( DuplicationStats.DUPLICATION_PERCENT_METRIC, timeMachine, cell ),
                        getTimeValue( DuplicationStats.DUPLICATED_LINES_METRIC, timeMachine, cell ),
                        getTimeValue( DuplicationStats.DUPLICATED_BLOCKS_METRIC, timeMachine, cell ),
                        getTimeValue( DuplicationStats.DUPLICATED_FILES_METRIC, timeMachine, cell ), cell.getDate() ) );
            }
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }

        return stats;
    }


    @Override
    public Set<TimeQuantitativeStats> getTimeQuantitativeStats( final String resourceId, final Date fromDate,
                                                                final Date toDate ) throws SonarManagerException
    {
        Set<TimeQuantitativeStats> stats = Sets.newHashSet();

        try
        {
            TimeMachine timeMachine = sonarClient.find( TimeMachineQuery
                    .createForMetrics( resourceId, QuantitativeStats.LINES_OF_CODE_METRIC,
                            QuantitativeStats.LINES_METRIC, QuantitativeStats.FILES_METRIC,
                            QuantitativeStats.DIRECTORIES_METRIC, QuantitativeStats.FUNCTIONS_METRIC,
                            QuantitativeStats.CLASSES_METRIC, QuantitativeStats.STATEMENTS_METRIC,
                            QuantitativeStats.ACCESSORS_METRIC ).setFrom( fromDate ).setTo( toDate ) );

            for ( TimeMachineCell cell : timeMachine.getCells() )
            {
                stats.add( new TimeQuantitativeStats(
                        getTimeValue( QuantitativeStats.LINES_OF_CODE_METRIC, timeMachine, cell ),
                        getTimeValue( QuantitativeStats.LINES_METRIC, timeMachine, cell ),
                        getTimeValue( QuantitativeStats.FILES_METRIC, timeMachine, cell ),
                        getTimeValue( QuantitativeStats.DIRECTORIES_METRIC, timeMachine, cell ),
                        getTimeValue( QuantitativeStats.FUNCTIONS_METRIC, timeMachine, cell ),
                        getTimeValue( QuantitativeStats.CLASSES_METRIC, timeMachine, cell ),
                        getTimeValue( QuantitativeStats.STATEMENTS_METRIC, timeMachine, cell ),
                        getTimeValue( QuantitativeStats.ACCESSORS_METRIC, timeMachine, cell ), cell.getDate() ) );
            }
        }
        catch ( Exception e )
        {
            throw new SonarManagerException( e );
        }

        return stats;
    }
}
