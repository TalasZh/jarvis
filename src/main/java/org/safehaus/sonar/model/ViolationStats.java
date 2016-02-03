package org.safehaus.sonar.model;


import com.google.common.base.Objects;


public class ViolationStats
{
    public static final String TECHNICAL_DEBT_METRIC = "sqale_index";
    public static final String OPEN_ISSUES_METRIC = "open_issues";
    public static final String REOPENED_ISSUES_METRIC = "reopened_issues";
    public static final String ALL_ISSUES_METRIC = "violations";
    public static final String BLOCKER_ISSUES_METRIC = "blocker_violations";
    public static final String CRITICAL_ISSUES_METRIC = "critical_violations";
    public static final String MAJOR_ISSUES_METRIC = "major_violations";
    public static final String MINOR_ISSUES_METRIC = "minor_violations";
    public static final String INFO_ISSUES_METRIC = "info_violations";

    private double technicalDebtMin;
    private double openIssues;
    private double reopenedIssues;
    private double allIssues;
    private double blockerIssues;
    private double criticalIssues;
    private double majorIssues;
    private double minorIssues;
    private double infoIssues;


    public ViolationStats( final double technicalDebtMin, final double openIssues, final double reopenedIssues,
                           final double allIssues, final double blockerIssues, final double criticalIssues,
                           final double majorIssues, final double minorIssues, final double infoIssues )
    {
        this.technicalDebtMin = technicalDebtMin;
        this.openIssues = openIssues;
        this.reopenedIssues = reopenedIssues;
        this.allIssues = allIssues;
        this.blockerIssues = blockerIssues;
        this.criticalIssues = criticalIssues;
        this.majorIssues = majorIssues;
        this.minorIssues = minorIssues;
        this.infoIssues = infoIssues;
    }


    /**
     * Returns technical debt in days (8h work day)
     *
     * @return technical debt in days
     */
    public double getTechnicalDebt()
    {
        return technicalDebtMin / ( 60 * 8 );
    }


    public double getOpenIssues()
    {
        return openIssues;
    }


    public double getReopenedIssues()
    {
        return reopenedIssues;
    }


    public double getAllIssues()
    {
        return allIssues;
    }


    public double getBlockerIssues()
    {
        return blockerIssues;
    }


    public double getCriticalIssues()
    {
        return criticalIssues;
    }


    public double getMajorIssues()
    {
        return majorIssues;
    }


    public double getMinorIssues()
    {
        return minorIssues;
    }


    public double getInfoIssues()
    {
        return infoIssues;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "technicalDebt(days)", getTechnicalDebt() )
                      .add( "openIssues", openIssues ).add( "reopenedIssues", reopenedIssues )
                      .add( "allIssues", allIssues ).add( "blockerIssues", blockerIssues )
                      .add( "criticalIssues", criticalIssues ).add( "majorIssues", majorIssues )
                      .add( "minorIssues", minorIssues ).add( "infoIssues", infoIssues ).toString();
    }
}
