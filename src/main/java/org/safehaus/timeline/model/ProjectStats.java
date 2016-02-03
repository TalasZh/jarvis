package org.safehaus.timeline.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.safehaus.dao.entities.sonar.SonarMetricIssue;


/**
 * Created by talas on 10/3/15.
 */
@Embeddable
public class ProjectStats
{
    @Column( name = "success_percent" )
    private double successPercent = 0;

    @Column( name = "failures" )
    private double failures = 0;

    @Column( name = "errors" )
    private double errors = 0;

    @Column( name = "test_count" )
    private double testCount = 0;

    @Column( name = "coverage_percent" )
    private double coveragePercent = 0;

    @Column( name = "all_issues" )
    private double allIssues = 0;

    @Column( name = "blocker_issues" )
    private double blockerIssues = 0;

    @Column( name = "critical_issues" )
    private double criticalIssues = 0;

    @Column( name = "major_issues" )
    private double majorIssues = 0;

    @Column( name = "classes_count" )
    private double classesCount = 0;

    @Column( name = "functions_count" )
    private double functionsCount = 0;

    @Column( name = "files_count" )
    private double filesCount = 0;

    @Column( name = "lines_of_code" )
    private double linesOfCode = 0;


    public ProjectStats()
    {
    }


    public ProjectStats( final SonarMetricIssue sonarMetricIssue )
    {
        this.linesOfCode = sonarMetricIssue.getLinesOfCode();
        this.filesCount = sonarMetricIssue.getFilesCount();
        this.functionsCount = sonarMetricIssue.getFunctionsCount();
        this.classesCount = sonarMetricIssue.getClassesCount();
        this.majorIssues = sonarMetricIssue.getMajorIssues();
        this.criticalIssues = sonarMetricIssue.getCriticalIssues();
        this.blockerIssues = sonarMetricIssue.getBlockerIssues();
        this.allIssues = sonarMetricIssue.getAllIssues();
        this.coveragePercent = sonarMetricIssue.getCoveragePercent();
        this.testCount = sonarMetricIssue.getTestCount();
        this.errors = sonarMetricIssue.getErrors();
        this.failures = sonarMetricIssue.getFailures();
        this.successPercent = sonarMetricIssue.getSuccessPercent();
    }


    public double getSuccessPercent()
    {
        return successPercent;
    }


    public void setSuccessPercent( final double successPercent )
    {
        this.successPercent = successPercent;
    }


    public double getFailures()
    {
        return failures;
    }


    public void setFailures( final double failures )
    {
        this.failures = failures;
    }


    public double getErrors()
    {
        return errors;
    }


    public void setErrors( final double errors )
    {
        this.errors = errors;
    }


    public double getTestCount()
    {
        return testCount;
    }


    public void setTestCount( final double testCount )
    {
        this.testCount = testCount;
    }


    public double getCoveragePercent()
    {
        return coveragePercent;
    }


    public void setCoveragePercent( final double coveragePercent )
    {
        this.coveragePercent = coveragePercent;
    }


    public double getAllIssues()
    {
        return allIssues;
    }


    public void setAllIssues( final double allIssues )
    {
        this.allIssues = allIssues;
    }


    public double getBlockerIssues()
    {
        return blockerIssues;
    }


    public void setBlockerIssues( final double blockerIssues )
    {
        this.blockerIssues = blockerIssues;
    }


    public double getCriticalIssues()
    {
        return criticalIssues;
    }


    public void setCriticalIssues( final double criticalIssues )
    {
        this.criticalIssues = criticalIssues;
    }


    public double getMajorIssues()
    {
        return majorIssues;
    }


    public void setMajorIssues( final double majorIssues )
    {
        this.majorIssues = majorIssues;
    }


    public double getClassesCount()
    {
        return classesCount;
    }


    public void setClassesCount( final double classesCount )
    {
        this.classesCount = classesCount;
    }


    public double getFunctionsCount()
    {
        return functionsCount;
    }


    public void setFunctionsCount( final double functionsCount )
    {
        this.functionsCount = functionsCount;
    }


    public double getFilesCount()
    {
        return filesCount;
    }


    public void setFilesCount( final double filesCount )
    {
        this.filesCount = filesCount;
    }


    public double getLinesOfCode()
    {
        return linesOfCode;
    }


    public void setLinesOfCode( final double linesOfCode )
    {
        this.linesOfCode = linesOfCode;
    }


    @Override
    public String toString()
    {
        return "ProjectStats{" +
                "successPercent=" + successPercent +
                ", failures=" + failures +
                ", errors=" + errors +
                ", testCount=" + testCount +
                ", coveragePercent=" + coveragePercent +
                ", allIssues=" + allIssues +
                ", blockerIssues=" + blockerIssues +
                ", criticalIssues=" + criticalIssues +
                ", majorIssues=" + majorIssues +
                ", classesCount=" + classesCount +
                ", functionsCount=" + functionsCount +
                ", filesCount=" + filesCount +
                ", linesOfCode=" + linesOfCode +
                '}';
    }
}
