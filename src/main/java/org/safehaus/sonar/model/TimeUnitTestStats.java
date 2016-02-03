package org.safehaus.sonar.model;


import java.util.Date;

import com.google.common.base.Objects;


public class TimeUnitTestStats extends UnitTestStats
{
    private Date date;


    public TimeUnitTestStats( final double successPercent, final double failures, final double errors,
                              final double testsCount, final double executionTimeMs, final double coveragePercent,
                              final double lineCoveragePercent, final double branchCoveragePercent, final Date date )
    {
        super( successPercent, failures, errors, testsCount, executionTimeMs, coveragePercent, lineCoveragePercent,
                branchCoveragePercent );
        this.date = date;
    }


    public Date getDate()
    {
        return date;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "successPercent", getSuccessPercent() )
                      .add( "failures", getFailures() ).add( "errors", getErrors() )
                      .add( "testsCount", getTestsCount() ).add( "executionTime(sec)", getExecutionTime() )
                      .add( "coveragePercent", getCoveragePercent() )
                      .add( "lineCoveragePercent", getLineCoveragePercent() )
                      .add( "branchCoveragePercent", getBranchCoveragePercent() ).add( "date", getDate() ).toString();
    }
}
