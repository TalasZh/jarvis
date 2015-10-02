package org.safehaus.dao.entities.sonar;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;


/**
 * Created by ermek on 9/30/15.
 */
@Entity
@Access( AccessType.FIELD )
@Table( name = "sonar_metric_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "projectId" ), @Index( name = "projectName" )
} )
public class SonarMetricIssue implements Serializable
{
    @Id
    @Column( name = "project_id" )
    private int projectId;

    @Column( name = "project_name" )
    private String projectName;

    @Column( name = "success_percent" )
    private double successPercent;

    @Column( name = "failures" )
    private double failures;

    @Column( name = "errors" )
    private double errors;

    @Column( name = "test_count" )
    private double testCount;

    @Column( name = "coverage_percent" )
    private double coveragePercent;

    @Column( name = "all_issues" )
    private double allIssues;

    @Column( name = "blocker_issues" )
    private double blockerIssues;

    @Column( name = "critical_issues" )
    private double criticalIssues;

    @Column( name = "classes_count" )
    private double classesCount;

    @Column( name = "functions_count" )
    private double functionsCount;

    @Column( name = "files_count" )
    private double filesCount;


    public int getProjectId()
    {
        return projectId;
    }


    public void setProjectId( final int projectId )
    {
        this.projectId = projectId;
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


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        final SonarMetricIssue that = ( SonarMetricIssue ) o;

        if ( projectId != that.projectId )
        {
            return false;
        }

        return true;
    }


    @Override
    public int hashCode()
    {
        return projectId;
    }


    public String getProjectName()
    {
        return projectName;
    }


    public void setProjectName( final String projectName )
    {
        this.projectName = projectName;
    }
}
