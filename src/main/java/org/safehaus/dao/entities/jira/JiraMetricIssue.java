package org.safehaus.dao.entities.jira;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Lists;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import net.rcarz.jiraclient.ChangeLog;
import net.rcarz.jiraclient.ChangeLogEntry;
import net.rcarz.jiraclient.ChangeLogItem;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.IssueLink;


/**
 * Created by kisik on 06.07.2015.
 */
@Entity
@Table( name = "jira_metric_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "issue_id" ), @Index( name = "issue_key" )
} )
public class JiraMetricIssue implements Serializable
{
    @Id
    @Column( name = "issue_id" )
    private Long issueId;

    @Column( name = "issue_key" )
    private String issueKey;

    @Column( name = "status" )
    private String status;

    @Column( name = "issue_status" )
    private String issueType;

    @Column( name = "project_key" )
    private String projectKey;

    @Column( name = "summary" )
    private String summary;

    @Column( name = "description" )
    private String description;

    @Embedded
    private JarvisIssueType type;

    @Column( name = "reporter_name" )
    private String reporterName;

    @Column( name = "assignee_name" )
    private String assigneeName;

    @Column( name = "resolution" )
    private String resolution;

    @Column( name = "creation_date" )
    private Date creationDate;

    @Column( name = "update_date" )
    private Date updateDate;

    @Column( name = "due_date" )
    private Date dueDate;

    @Column( name = "priority" )
    private Long priority;

    @Column( name = "original_estimate_in_minutes" )
    private Integer originalEstimateMinutes;

    @Column( name = "remaining_estimate_in_minutes" )
    private Integer remainingEstimateMinutes;

    @Column( name = "time_spent_in_minutes" )
    private Integer timeSpentMinutes;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "changelog_id" )
    private List<JiraIssueChangelog> changelogList = Lists.newArrayList();


    public JiraMetricIssue()
    {
    }


    public JiraMetricIssue( final Issue issue )
    {
        if ( issue.getKey() != null )
        {
            this.setIssueKey( issue.getKey() );
        }
        if ( issue.getId() != null )
        {
            this.setIssueId( Long.valueOf( issue.getId() ) );
        }
        if ( issue.getStatus() != null && issue.getStatus().getName() != null )
        {
            this.setStatus( issue.getStatus().getName() );
        }
        if ( issue.getProject() != null && issue.getProject().getKey() != null )
        {
            this.setProjectKey( issue.getProject().getKey() );
        }
        if ( issue.getReporter() != null && issue.getReporter().getName() != null )
        {
            this.setReporterName( issue.getReporter().getName() );
        }
        if ( issue.getAssignee() != null && issue.getAssignee().getName() != null )
        {
            this.setAssigneeName( issue.getAssignee().getName() );
        }
        if ( issue.getResolution() != null && issue.getResolution().getName() != null )
        {
            this.setResolution( issue.getResolution().getName() );
        }
        if ( issue.getCreatedDate() != null )
        {
            this.setCreationDate( issue.getCreatedDate() );
        }
        if ( issue.getUpdatedDate() != null )
        {
            this.setUpdateDate( issue.getUpdatedDate() );
        }
        if ( issue.getDueDate() != null )
        {
            this.setDueDate( issue.getDueDate() );
        }
        if ( issue.getPriority() != null && issue.getPriority().getId() != null )
        {
            this.setPriority( Long.valueOf( issue.getPriority().getId() ) );
        }
        if ( issue.getTimeTracking() != null )
        {
            this.setOriginalEstimateMinutes( issue.getTimeTracking().getOriginalEstimateSeconds() / 60 );
        }
        if ( issue.getTimeTracking() != null )
        {
            this.setRemainingEstimateMinutes( issue.getTimeTracking().getRemainingEstimateSeconds() / 60 );
        }
        if ( issue.getTimeTracking() != null )
        {
            this.setTimeSpentMinutes( issue.getTimeTracking().getTimeSpentSeconds() / 60 );
        }
        if ( issue.getChangeLog() != null )
        {
            final List<JiraIssueChangelog> changelogList = Lists.newArrayList();
            final ChangeLog changeLog = issue.getChangeLog();

            for ( final ChangeLogEntry changeLogEntry : changeLog.getEntries() )
            {
                for ( final ChangeLogItem changeLogItem : changeLogEntry.getItems() )
                {
                    ChangeCompositeKey changeCompositeKey =
                            new ChangeCompositeKey( changeLogItem.getId(), changeLogEntry.getCreated().getTime() );
                    JiraIssueChangelog jiraIssueChangelog =
                            new JiraIssueChangelog( changeCompositeKey, issue.getKey(), Long.valueOf( issue.getId() ),
                                    changeLogEntry.getAuthor().getDisplayName(), changeLogItem.getFieldType(),
                                    changeLogItem.getField(), changeLogItem.getFromString(),
                                    changeLogItem.getToString(), changeLogItem.getTo(), changeLogItem.getToString() );
                    changelogList.add( jiraIssueChangelog );
                }
            }
            this.setChangelogList( changelogList );
        }

        if ( issue.getIssueLinks() != null )
        {
            for ( final IssueLink issueLink : issue.getIssueLinks() )
            {

            }
        }
    }


    public String getIssueKey()
    {
        return issueKey;
    }


    public void setIssueKey( String key )
    {
        this.issueKey = key;
    }


    public Long getIssueId()
    {
        return issueId;
    }


    public void setIssueId( Long id )
    {
        this.issueId = id;
    }


    public String getStatus()
    {
        return status;
    }


    public void setStatus( String status )
    {
        this.status = status;
    }


    public String getIssueType()
    {
        return issueType;
    }


    public void setIssueType( String issueType )
    {
        this.issueType = issueType;
    }


    public String getProjectKey()
    {
        return projectKey;
    }


    public void setProjectKey( String projectKey )
    {
        this.projectKey = projectKey;
    }


    public String getReporterName()
    {
        return reporterName;
    }


    public void setReporterName( String reporterName )
    {
        this.reporterName = reporterName;
    }


    public String getAssigneeName()
    {
        return assigneeName;
    }


    public void setAssigneeName( String assigneeName )
    {
        this.assigneeName = assigneeName;
    }


    public String getResolution()
    {
        return resolution;
    }


    public void setResolution( String resolution )
    {
        this.resolution = resolution;
    }


    public Date getCreationDate()
    {
        return creationDate;
    }


    public void setCreationDate( Date creationDate )
    {
        this.creationDate = creationDate;
    }


    public Date getUpdateDate()
    {
        return updateDate;
    }


    public void setUpdateDate( Date updateDate )
    {
        this.updateDate = updateDate;
    }


    public Date getDueDate()
    {
        return dueDate;
    }


    public void setDueDate( Date dueDate )
    {
        this.dueDate = dueDate;
    }


    public Long getPriority()
    {
        return priority;
    }


    public void setPriority( Long priority )
    {
        this.priority = priority;
    }


    public Integer getOriginalEstimateMinutes()
    {
        return originalEstimateMinutes;
    }


    public void setOriginalEstimateMinutes( Integer originalEstimateMinutes )
    {
        this.originalEstimateMinutes = originalEstimateMinutes;
    }


    public Integer getRemainingEstimateMinutes()
    {
        return remainingEstimateMinutes;
    }


    public void setRemainingEstimateMinutes( Integer remainingEstimateMinutes )
    {
        this.remainingEstimateMinutes = remainingEstimateMinutes;
    }


    public Integer getTimeSpentMinutes()
    {
        return timeSpentMinutes;
    }


    public void setTimeSpentMinutes( Integer timeSpentMinutes )
    {
        this.timeSpentMinutes = timeSpentMinutes;
    }


    public List<JiraIssueChangelog> getChangelogList()
    {
        return changelogList;
    }


    public void setChangelogList( final List<JiraIssueChangelog> changelogList )
    {
        this.changelogList = changelogList;
    }


    @Override
    public String toString()
    {
        return "JiraMetricIssue{" +
                "issueKey='" + issueKey + '\'' +
                ", issueId=" + issueId +
                ", status='" + status + '\'' +
                ", issueType='" + issueType + '\'' +
                ", projectKey='" + projectKey + '\'' +
                ", reporterName='" + reporterName + '\'' +
                ", assigneeName='" + assigneeName + '\'' +
                ", resolution='" + resolution + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", originalEstimateMinutes=" + originalEstimateMinutes +
                ", remainingEstimateMinutes=" + remainingEstimateMinutes +
                ", timeSpentMinutes=" + timeSpentMinutes +
                '}';
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JiraMetricIssue ) )
        {
            return false;
        }

        final JiraMetricIssue that = ( JiraMetricIssue ) o;

        return issueId.equals( that.issueId );
    }


    @Override
    public int hashCode()
    {
        return issueId.hashCode();
    }
}
