package org.safehaus.sonar.model;


import java.util.Date;

import com.google.common.base.Objects;


public class TimeViolationStats extends ViolationStats
{
    private Date date;


    public TimeViolationStats( final double technicalDebtMin, final double openIssues, final double reopenedIssues,
                               final double allIssues, final double blockerIssues, final double criticalIssues,
                               final double majorIssues, final double minorIssues, final double infoIssues,
                               final Date date )
    {
        super( technicalDebtMin, openIssues, reopenedIssues, allIssues, blockerIssues, criticalIssues, majorIssues,
                minorIssues, infoIssues );
        this.date = date;
    }


    public Date getDate()
    {
        return date;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "technicalDebt(days)", getTechnicalDebt() )
                      .add( "openIssues", getOpenIssues() ).add( "reopenedIssues", getReopenedIssues() )
                      .add( "allIssues", getAllIssues() ).add( "blockerIssues", getBlockerIssues() )
                      .add( "criticalIssues", getCriticalIssues() ).add( "majorIssues", getMajorIssues() )
                      .add( "minorIssues", getMinorIssues() ).add( "infoIssues", getInfoIssues() )
                      .add( "date", getDate() ).toString();
    }
}
