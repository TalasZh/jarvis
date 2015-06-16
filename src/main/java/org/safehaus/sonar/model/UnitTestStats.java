package org.safehaus.sonar.model;


import com.google.common.base.Objects;


public class UnitTestStats
{
    public static final String SUCCESS_PERCENT_METRIC = "test_success_density";
    public static final String FAILURES_METRIC = "test_failures";
    public static final String ERRORS_METRIC = "test_errors";
    public static final String TESTS_COUNT_METRIC = "tests";
    public static final String EXEC_TIME_METRIC = "test_execution_time";
    public static final String COVERAGE_METRIC = "coverage";
    public static final String LINE_COVERAGE_METRIC = "line_coverage";
    public static final String BRANCH_COVERAGE_METRIC = "branch_coverage";


    private double successPercent;
    private double failures;
    private double errors;
    private double testsCount;
    private double executionTimeMs;
    private double coveragePercent;
    private double lineCoveragePercent;
    private double branchCoveragePercent;


    public UnitTestStats( final double successPercent, final double failures, final double errors,
                          final double testsCount, final double executionTimeMs, final double coveragePercent,
                          final double lineCoveragePercent, final double branchCoveragePercent )
    {
        this.successPercent = successPercent;
        this.failures = failures;
        this.errors = errors;
        this.testsCount = testsCount;
        this.executionTimeMs = executionTimeMs;
        this.coveragePercent = coveragePercent;
        this.lineCoveragePercent = lineCoveragePercent;
        this.branchCoveragePercent = branchCoveragePercent;
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


    /**
     * Returns unit test execution time in seconds
     *
     * @return execution time in seconds
     */
    public double getExecutionTime()
    {
        return executionTimeMs / 1000;
    }


    public double getCoveragePercent()
    {
        return coveragePercent;
    }


    public double getLineCoveragePercent()
    {
        return lineCoveragePercent;
    }


    public double getBranchCoveragePercent()
    {
        return branchCoveragePercent;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "successPercent", successPercent ).add( "failures", failures )
                      .add( "errors", errors ).add( "testsCount", testsCount )
                      .add( "executionTime(sec)", getExecutionTime() ).add( "coveragePercent", coveragePercent )
                      .add( "lineCoveragePercent", lineCoveragePercent )
                      .add( "branchCoveragePercent", branchCoveragePercent ).toString();
    }
}
