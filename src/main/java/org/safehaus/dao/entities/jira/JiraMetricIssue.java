package org.safehaus.dao.entities.jira;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import net.rcarz.jiraclient.ChangeLog;
import net.rcarz.jiraclient.ChangeLogEntry;
import net.rcarz.jiraclient.ChangeLogItem;
import net.rcarz.jiraclient.Component;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.IssueLink;
import net.rcarz.jiraclient.IssueType;
import net.rcarz.jiraclient.RemoteLink;
import net.rcarz.jiraclient.WorkLog;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by kisik on 06.07.2015.
 */
@Entity
@Access( AccessType.FIELD )
@Table( name = "jira_metric_issue", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "issueId" ), @Index( name = "issueKey" ), @Index( name = "assigneeName" ),
        @Index( name = "projectKey" ), @Index( name = "status" ), @Index( name = "typeName" )
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

    @Column( name = "project_key" )
    private String projectKey;

    @Column( name = "summary" )
    private String summary;

    @Column( name = "description" )
    private String description;

    @Column( name = "reporter_name" )
    private String reporterName;

    @Column( name = "assignee_name" )
    private String assigneeName;

    @Column( name = "resolution" )
    private String resolution;

    @Column( name = "creation_date" )
    private Long creationDate;

    @Column( name = "update_date" )
    private Long updateDate;

    @Column( name = "due_date" )
    private Long dueDate;

    @Column( name = "priority" )
    private Long priority;

    @Column( name = "original_estimate_in_minutes" )
    private Integer originalEstimateMinutes;

    @Column( name = "remaining_estimate_in_minutes" )
    private Integer remainingEstimateMinutes;

    @Column( name = "time_spent_in_minutes" )
    private Integer timeSpentMinutes;

    @Embedded
    private JarvisIssueType type;

    @Column( name = "type_id" )
    private Long typeId;

    @Column( name = "type_name" )
    private String typeName;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "changelog_id" )
    private List<JiraIssueChangelog> changelogList = Lists.newArrayList();

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "issue_link_id" )
    private List<JarvisLink> issueLinks = Lists.newArrayList();

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "issue_id" )
    private List<IssueWorkLog> issueWorkLogs = Lists.newArrayList();

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "issue_id" )
    private List<IssueRemoteLink> remoteLinks = Lists.newArrayList();

    @ElementCollection
    @Column( name = "components" )
    private Set<String> components = Sets.newHashSet();

    @ElementCollection
    @Column( name = "labels" )
    private Set<String> labels = Sets.newHashSet();

    @ElementCollection
    @Column( name = "commits" )
    private Set<String> gitCommits = Sets.newHashSet();


    public JiraMetricIssue()
    {
    }


    public JiraMetricIssue( final Issue issue )
    {
        Preconditions.checkNotNull( issue, "Issue cannot be null..." );
        this.issueKey = issue.getKey();
        this.summary = issue.getSummary();
        this.description = issue.getDescription();
        this.issueId = Long.valueOf( issue.getId() );
        if ( issue.getStatus() != null )
        {
            this.status = issue.getStatus().getName();
        }
        if ( issue.getProject() != null )
        {
            this.projectKey = issue.getProject().getKey();
        }
        if ( issue.getReporter() != null )
        {
            this.reporterName = issue.getReporter().getName();
        }
        if ( issue.getAssignee() != null )
        {
            this.assigneeName = issue.getAssignee().getName();
        }
        if ( issue.getResolution() != null )
        {
            this.resolution = issue.getResolution().getName();
        }
        this.creationDate = issue.getCreatedDate() != null ? issue.getCreatedDate().getTime() : 0;
        this.updateDate = issue.getUpdatedDate() != null ? issue.getUpdatedDate().getTime() : 0;
        this.dueDate = issue.getDueDate() != null ? issue.getDueDate().getTime() : 0;
        if ( issue.getPriority() != null )
        {
            this.priority = issue.getPriority().getId() != null ? Long.valueOf( issue.getPriority().getId() ) : 0L;
        }
        if ( issue.getTimeTracking() != null )
        {
            this.originalEstimateMinutes = issue.getTimeTracking().getOriginalEstimateSeconds() / 60;
            this.remainingEstimateMinutes = issue.getTimeTracking().getRemainingEstimateSeconds() / 60;
            this.timeSpentMinutes = issue.getTimeTracking().getTimeSpentSeconds() / 60;
        }
        if ( issue.getIssueType() != null )
        {
            IssueType jiraIssueType = issue.getIssueType();
            this.type = new JarvisIssueType( Long.valueOf( jiraIssueType.getId() ), jiraIssueType.getName() );
            this.typeId = Long.valueOf( jiraIssueType.getId() );
            this.typeName = jiraIssueType.getName();
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
                            new ChangeCompositeKey( Long.valueOf( changeLogEntry.getId() ),
                                    changeLogEntry.getCreated().getTime() );
                    String issueKey = issue.getKey();
                    Long issueId = Long.valueOf( issue.getId() );
                    String author = changeLogEntry.getAuthor() != null ? changeLogEntry.getAuthor().getDisplayName() :
                                    "Unknown";

                    JiraIssueChangelog jiraIssueChangelog =
                            new JiraIssueChangelog( changeCompositeKey, issueKey, issueId, author,
                                    changeLogItem.getFieldType(), changeLogItem.getField(),
                                    changeLogItem.getFromString(), changeLogItem.getToString(), changeLogItem.getFrom(),
                                    changeLogItem.getTo() );
                    changelogList.add( jiraIssueChangelog );
                }
            }
            this.changelogList = changelogList;
        }

        try
        {
            for ( final WorkLog workLog : issue.getWorkLogs() )
            {
                IssueWorkLog issueWorkLog = new IssueWorkLog( workLog );
                this.issueWorkLogs.add( issueWorkLog );
            }


            for ( final RemoteLink remoteLink : issue.getRemoteLinks() )
            {
                IssueRemoteLink issueRemoteLink = new IssueRemoteLink( remoteLink.getTitle(), remoteLink.getRemoteUrl(),
                        String.format( "%s-%s", remoteLink.getId(), this.issueId ), remoteLink.getUrl() );
                remoteLinks.add( issueRemoteLink );
            }

            for ( final Component component : issue.getComponents() )
            {
                this.components.add( component.getName() );
            }

            this.labels.addAll( issue.getLabels() );
        }
        catch ( Exception e )
        {
            //ignore
            e.printStackTrace();
        }

        if ( issue.getIssueLinks() != null )
        {
            List<JarvisLink> jarvisIssueLinks = Lists.newArrayList();
            for ( final IssueLink issueLink : issue.getIssueLinks() )
            {
                LinkDirection linkDirection = new LinkDirection();
                Issue linkedIssue = issueLink.getInwardIssue();
                JarvisLink.Direction direction = JarvisLink.Direction.INWARD;

                if ( linkedIssue == null )
                {
                    linkedIssue = issueLink.getOutwardIssue();
                    direction = JarvisLink.Direction.OUTWARD;
                }

                linkDirection.setIssueId( Long.valueOf( linkedIssue.getId() ) );
                linkDirection.setIssueKey( linkedIssue.getKey() );

                IssueType linkedIssueType = linkedIssue.getIssueType();

                net.rcarz.jiraclient.LinkType jiraLinkType = issueLink.getType();


                if ( linkedIssueType != null && linkedIssueType != null )
                {
                    LinkType linkType = new LinkType( Long.valueOf( jiraLinkType.getId() ), jiraLinkType.getName(),
                            jiraLinkType.getInward(), jiraLinkType.getOutward() );

                    JarvisIssueType jarvisIssueType =
                            new JarvisIssueType( Long.valueOf( linkedIssueType.getId() ), linkedIssueType.getName() );

                    JarvisLink jarvisLink =
                            new JarvisLink( Long.valueOf( issueLink.getId() ), linkType, linkDirection, jarvisIssueType,
                                    direction );

                    jarvisIssueLinks.add( jarvisLink );
                }
            }
            this.issueLinks = jarvisIssueLinks;
        }
    }


    public Long getTypeId()
    {
        return typeId;
    }


    public void setTypeId( final Long typeId )
    {
        this.typeId = typeId;
    }


    public String getTypeName()
    {
        return typeName;
    }


    public void setTypeName( final String typeName )
    {
        this.typeName = typeName;
    }


    public Set<String> getGitCommits()
    {
        return gitCommits;
    }


    public void setGitCommits( final Set<String> gitCommits )
    {
        this.gitCommits = gitCommits;
    }


    public Set<String> getComponents()
    {
        return components;
    }


    public void setComponents( final Set<String> components )
    {
        this.components = components;
    }


    public Set<String> getLabels()
    {
        return labels;
    }


    public void setLabels( final Set<String> labels )
    {
        this.labels = labels;
    }


    public List<IssueRemoteLink> getRemoteLinks()
    {
        return remoteLinks;
    }


    public void setRemoteLinks( final List<IssueRemoteLink> remoteLinks )
    {
        this.remoteLinks = remoteLinks;
    }


    public List<IssueWorkLog> getIssueWorkLogs()
    {
        return issueWorkLogs;
    }


    public void setIssueWorkLogs( final List<IssueWorkLog> issueWorkLogs )
    {
        this.issueWorkLogs = issueWorkLogs;
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
        if ( creationDate == null )
        {
            return new Date();
        }
        return new Date( creationDate );
    }


    public void setCreationDate( Date creationDate )
    {
        this.creationDate = creationDate.getTime();
    }


    public Date getUpdateDate()
    {
        if ( updateDate == null )
        {
            return new Date();
        }
        return new Date( updateDate );
    }


    public void setUpdateDate( Date updateDate )
    {
        this.updateDate = updateDate.getTime();
    }


    public Date getDueDate()
    {
        if ( dueDate == null )
        {
            return new Date();
        }
        return new Date( dueDate );
    }


    public void setDueDate( Date dueDate )
    {
        this.dueDate = dueDate.getTime();
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


    public String getSummary()
    {
        return summary;
    }


    public void setSummary( final String summary )
    {
        this.summary = summary;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription( final String description )
    {
        this.description = description;
    }


    public JarvisIssueType getType()
    {
        return type;
    }


    public void setType( final JarvisIssueType type )
    {
        this.type = type;
    }


    public List<JarvisLink> getIssueLinks()
    {
        return issueLinks;
    }


    public void setIssueLinks( final List<JarvisLink> issueLinks )
    {
        this.issueLinks = issueLinks;
    }


    @Override
    public String toString()
    {
        return "JiraMetricIssue{" +
                "issueKey='" + issueKey + '\'' +
                ", issueId=" + issueId +
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

        return !( issueId != null ? !issueId.equals( that.issueId ) : that.issueId != null );
    }


    @Override
    public int hashCode()
    {
        return issueId != null ? issueId.hashCode() : 0;
    }
}
