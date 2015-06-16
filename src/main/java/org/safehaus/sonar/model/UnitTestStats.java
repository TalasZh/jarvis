package org.safehaus.sonar.model;


import com.google.common.base.Objects;


public class UnitTestStats
{
    public static final String SUCCESS_PERCENT_METRIC = "test_success_density";
    public static final String FAILURES_METRIC = "test_failures";
    public static final String ERRORS_METRIC = "test_errors";
    public static final String TESTS_COUNT_METRIC = "tests";
    public static final String EXEC_TIME_METRIC = "test_execution_time";


    private double successPercent;

    private double failures;

    private double errors;

    private double testsCount;

    private double executionTimeMs;


    public UnitTestStats( final double successPercent, final double failures, final double errors,
                          final double testsCount, final double executionTimeMs )
    {
        this.successPercent = successPercent;
        this.failures = failures;
        this.errors = errors;
        this.testsCount = testsCount;
        this.executionTimeMs = executionTimeMs;
    }


    public double getSuccessPercent()
    {
        return successPercent;
    }


    public double getFailures()
    {
        return failures;
    }


    public double getErrors()
    {
        return errors;
    }


    public double getTestsCount()
    {
        return testsCount;
    }


    public double getExecutionTimeMs()
    {
        return executionTimeMs;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "successPercent", successPercent ).add( "failures", failures )
                      .add( "errors", errors ).add( "testsCount", testsCount ).add( "executionTimeMs", executionTimeMs )
                      .toString();
    }
}
