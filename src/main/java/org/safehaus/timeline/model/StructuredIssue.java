package org.safehaus.timeline.model;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.safehaus.dao.entities.jira.IssueRemoteLink;
import org.safehaus.dao.entities.jira.IssueWorkLog;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by talas on 9/27/15.
 */
@Entity
@Access( AccessType.FIELD )
@Table( name = "structured_issue", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "key" ), @Index( name = "assignee" ), @Index( name = "reporter" )
} )
public class StructuredIssue implements Serializable, Structure
{
    @JsonView( Views.TimelineShort.class )
    @Id
    @Column( name = "structured_id" )
    private Long id;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "structured_key" )
    private String key;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "issue_type" )
    private String issueType;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "summary" )
    private String summary;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "description" )
    private String description;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "reporter" )
    private String reporter;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "creator" )
    private String creator;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "assignee" )
    private String assignee;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "updated" )
    private Long updated;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "original_estimate_min" )
    private int originalEstimateMinutes;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "created" )
    private Long created;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "status" )
    private String status;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "project_key" )
    private String projectKey;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "due_date" )
    private String dueDate;

    @Transient
    @JsonProperty( "issues" )
    @JsonView( Views.TimelineLong.class )
    private Set<StructuredIssue> issues = Sets.newHashSet();

    @JsonIgnore
    @ElementCollection
    @Column( name = "issues" )
    private Set<String> issuesKeys = Sets.newHashSet();

    @ElementCollection
    @Column( name = "usernames" )
    private Set<String> users = Sets.newHashSet();

    @ElementCollection
    @Column( name = "components" )
    private Set<String> components = Sets.newHashSet();

    @ElementCollection
    @Column( name = "labels" )
    private Set<String> labels = Sets.newHashSet();

    @Embedded
    private ProgressStatus openStatus = new ProgressStatus();

    @Embedded
    private ProgressStatus inProgressStatus = new ProgressStatus();

    @Embedded
    private ProgressStatus doneStatus = new ProgressStatus();

    @ElementCollection
    @MapKeyColumn( name = "issuesSolved" )
    @Column( name = "totalSolved" )
    @CollectionTable( name = "resolvedIssues", joinColumns = @JoinColumn( name = "solved_id" ) )
    Map<String, Long> totalIssuesSolved = Maps.newHashMap(); // maps from attribute name to value

    @Embedded
    private IssueProgress storyPoints = new IssueProgress();

    @Embedded
    private IssueProgress storyProgress = new IssueProgress();

    @Embedded
    private IssueProgress requirementProgress = new IssueProgress();

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "issue_id" )
    private List<StructuredIssueLink> remoteLinks = Lists.newArrayList();

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "issue_id" )
    private List<StructuredWorkLog> issueWorkLogs = Lists.newArrayList();


    public StructuredIssue()
    {
    }


    public StructuredIssue( final String key, final Long id, final String issueType, final String summary,
                            final String reporter, final String creator, final String assignee, final Long updated,
                            final Long created, final String status, final String projectKey, final String dueDate,
                            final List<IssueRemoteLink> remoteLinks, final Set<String> components,
                            final Set<String> labels, final String description, final int originalEstimateMinutes,
                            final List<IssueWorkLog> issueWorkLogs )
    {
        this.key = key;
        this.id = id;
        this.issueType = issueType;
        this.summary = summary;
        this.reporter = reporter;
        this.creator = creator;
        this.assignee = assignee;
        this.updated = updated;
        this.created = created;
        this.status = status;
        this.projectKey = projectKey;
        this.dueDate = dueDate;
        this.components = components;
        this.labels = labels;
        this.description = description;
        this.originalEstimateMinutes = originalEstimateMinutes;
        for ( final IssueWorkLog workLog : issueWorkLogs )
        {
            this.issueWorkLogs.add( new StructuredWorkLog( workLog ) );
        }
        for ( final IssueRemoteLink remoteLink : remoteLinks )
        {
            this.remoteLinks.add( new StructuredIssueLink( remoteLink.getTitle(), remoteLink.getRemoteUrl(),
                    String.format( "%s-%s", remoteLink.getId(), this.id ), remoteLink.getUrl() ) );
        }
    }


    @Override
    public IssueProgress getRequirementProgress()
    {
        return requirementProgress;
    }


    @Override
    public void setRequirementProgress( final IssueProgress requirementProgress )
    {
        this.requirementProgress = requirementProgress;
    }


    public List<StructuredWorkLog> getIssueWorkLogs()
    {
        return issueWorkLogs;
    }


    public void setIssueWorkLogs( final List<StructuredWorkLog> issueWorkLogs )
    {
        this.issueWorkLogs = issueWorkLogs;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription( final String description )
    {
        this.description = description;
    }


    public int getOriginalEstimateMinutes()
    {
        return originalEstimateMinutes;
    }


    public void setOriginalEstimateMinutes( final int originalEstimateMinutes )
    {
        this.originalEstimateMinutes = originalEstimateMinutes;
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


    public List<StructuredIssueLink> getRemoteLinks()
    {
        return remoteLinks;
    }


    public void setRemoteLinks( final List<StructuredIssueLink> remoteLinks )
    {
        this.remoteLinks = remoteLinks;
    }


    @Override
    public IssueProgress getStoryProgress()
    {
        return storyProgress;
    }


    @Override
    public void setStoryProgress( final IssueProgress storyProgress )
    {
        this.storyProgress = storyProgress;
    }


    public String getDueDate()
    {
        return dueDate;
    }


    public void setDueDate( final String dueDate )
    {
        this.dueDate = dueDate;
    }


    public String getProjectKey()
    {
        return projectKey;
    }


    public void setProjectKey( final String projectKey )
    {
        this.projectKey = projectKey;
    }


    public String getIssueType()
    {
        return issueType;
    }


    public String getReporter()
    {
        return reporter;
    }


    @Override
    public Set<String> getUsers()
    {
        return users;
    }


    @Override
    public void setUsers( final Set<String> usernames )
    {
        this.users = usernames;
    }


    @Override
    public IssueProgress getStoryPoints()
    {
        return storyPoints;
    }


    @Override
    public void setStoryPoints( final IssueProgress storyPoints )
    {
        this.storyPoints = storyPoints;
    }


    public Set<String> getIssuesKeys()
    {
        return issuesKeys;
    }


    public Set<StructuredIssue> getIssues()
    {
        return Collections.unmodifiableSet( issues );
    }


    public void addIssue( StructuredIssue structuredIssue )
    {
        this.issues.add( structuredIssue );
        this.issuesKeys.add( structuredIssue.getKey() );
    }


    @Override
    public ProgressStatus getOpenStatus()
    {
        return openStatus;
    }


    @Override
    public void setOpenStatus( final ProgressStatus openStatus )
    {
        this.openStatus = openStatus;
    }


    @Override
    public ProgressStatus getInProgressStatus()
    {
        return inProgressStatus;
    }


    @Override
    public void setInProgressStatus( final ProgressStatus inProgressStatus )
    {
        this.inProgressStatus = inProgressStatus;
    }


    @Override
    public ProgressStatus getDoneStatus()
    {
        return doneStatus;
    }


    @Override
    public void setDoneStatus( final ProgressStatus doneStatus )
    {
        this.doneStatus = doneStatus;
    }


    public String getKey()
    {
        return key;
    }


    public String getStatus()
    {
        return status;
    }


    public Map<String, Long> getTotalIssuesSolved()
    {
        return totalIssuesSolved;
    }


    public void setTotalIssuesSolved( final Map<String, Long> totalIssuesSolved )
    {
        this.totalIssuesSolved = totalIssuesSolved;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof StructuredIssue ) )
        {
            return false;
        }

        final StructuredIssue that = ( StructuredIssue ) o;

        return !( id != null ? !id.equals( that.id ) : that.id != null );
    }


    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "StructuredIssue{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", issueType='" + issueType + '\'' +
                ", summary='" + summary + '\'' +
                ", reporter='" + reporter + '\'' +
                ", creator='" + creator + '\'' +
                ", assignee='" + assignee + '\'' +
                ", updated=" + updated +
                ", created=" + created +
                ", status='" + status + '\'' +
                ", issues=" + issues +
                ", openStatus=" + openStatus +
                ", inProgressStatus=" + inProgressStatus +
                ", doneStatus=" + doneStatus +
                '}';
    }
}
