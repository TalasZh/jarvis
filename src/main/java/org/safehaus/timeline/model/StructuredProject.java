package org.safehaus.timeline.model;


import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.dao.entities.jira.ProjectVersion;
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
@XmlRootElement
@Entity
@Access( AccessType.FIELD )
@Table( name = "structured_project", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "id" ), @Index( name = "key" )
} )
public class StructuredProject implements Serializable, Structure
{
    @JsonView( Views.TimelineShort.class )
    @Id
    @Column( name = "project_id" )
    private String id;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "project_name" )
    private String name;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "project_key" )
    private String key;

    /**
     * Issues in project with structured representation
     */
    @Transient
    @JsonProperty( "issues" )
    @JsonView( Views.TimelineLong.class )
    private Set<StructuredIssue> issues = Sets.newHashSet();

    @JsonView( Views.TimelineShort.class )
    @Column( name = "project_description" )
    private String description;

    /**
     * Issue keys to store structure in database
     */
    @JsonIgnore
    @ElementCollection
    @Column( name = "issues" )
    private Set<String> issuesKeys = Sets.newHashSet();

    @Column( name = "epics_count" )
    private long epicsCount;

    /**
     * List of users participated in project
     */
    @JsonView( Views.TimelineShort.class )
    @ElementCollection
    @Column( name = "usernames" )
    private Set<String> users = Sets.newHashSet();

    @Embedded
    private IssueProgress storyPoints = new IssueProgress();

    @Embedded
    private IssueProgress storyProgress = new IssueProgress();

    @Embedded
    private ProgressStatus openStatus = new ProgressStatus();

    @Embedded
    private ProgressStatus inProgressStatus = new ProgressStatus();

    @Embedded
    private ProgressStatus doneStatus = new ProgressStatus();

    @Transient
    private List<ProjectVersion> projectVersions = Lists.newArrayList();


    @ElementCollection
    @MapKeyColumn( name = "issuesSolved" )
    @Column( name = "totalSolved" )
    @CollectionTable( name = "resolvedIssues", joinColumns = @JoinColumn( name = "solved_id" ) )
    private Map<String, Long> totalIssuesSolved = Maps.newHashMap(); // maps from attribute name to value


    @ElementCollection
    @MapKeyColumn( name = "epicsCompleted" )
    @Column( name = "epicsSolved" )
    @CollectionTable( name = "epics_completion", joinColumns = @JoinColumn( name = "project_id" ) )
    private Map<String, Long> epicCompletion = Maps.newHashMap(); // maps from attribute name to value


    @Embedded
    private ProjectStats projectStats = new ProjectStats();


    @Embedded
    private IssueProgress requirementProgress = new IssueProgress();


    public StructuredProject()
    {

    }


    public StructuredProject( final String projectId, final String name, final String key, final String description,
                              final List<ProjectVersion> projectVersions )
    {
        this.id = projectId;
        this.name = name;
        this.key = key;
        this.description = description;
        this.projectVersions = projectVersions;
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


    public Map<String, Long> getEpicCompletion()
    {
        return epicCompletion;
    }


    public void setEpicCompletion( final Map<String, Long> epicCompletion )
    {
        this.epicCompletion = epicCompletion;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription( final String description )
    {
        this.description = description;
    }


    public List<ProjectVersion> getProjectVersions()
    {
        return projectVersions;
    }


    public void setProjectVersions( final List<ProjectVersion> projectVersions )
    {
        this.projectVersions = projectVersions;
    }


    public ProjectStats getProjectStats()
    {
        return projectStats;
    }


    public void setProjectStats( final ProjectStats projectStats )
    {
        this.projectStats = projectStats;
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


    public long getEpicsCount()
    {
        return epicsCount;
    }


    public void setEpicsCount( final long epicsCount )
    {
        this.epicsCount = epicsCount;
    }


    public void addIssue( StructuredIssue structuredIssue )
    {
        this.issues.add( structuredIssue );
        this.issuesKeys.add( structuredIssue.getKey() );
    }


    public Set<String> getIssuesKeys()
    {
        return issuesKeys;
    }


    public ProgressStatus getOpenStatus()
    {
        return openStatus;
    }


    public void setOpenStatus( final ProgressStatus openStatus )
    {
        this.openStatus = openStatus;
    }


    public ProgressStatus getInProgressStatus()
    {
        return inProgressStatus;
    }


    public void setInProgressStatus( final ProgressStatus inProgressStatus )
    {
        this.inProgressStatus = inProgressStatus;
    }


    public ProgressStatus getDoneStatus()
    {
        return doneStatus;
    }


    public void setDoneStatus( final ProgressStatus doneStatus )
    {
        this.doneStatus = doneStatus;
    }


    public Set<StructuredIssue> getIssues()
    {
        return Collections.unmodifiableSet( issues );
    }


    public void setIssues( final Set<StructuredIssue> issues )
    {
        this.issues = issues;
        for ( final StructuredIssue issue : issues )
        {
            issuesKeys.add( issue.getKey() );
        }
    }


    @Override
    public Map<String, Long> getTotalIssuesSolved()
    {
        return totalIssuesSolved;
    }


    @Override
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
        if ( !( o instanceof StructuredProject ) )
        {
            return false;
        }

        final StructuredProject that = ( StructuredProject ) o;

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
        return "StructuredProject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", issues=" + issues +
                ", openStatus=" + openStatus +
                ", inProgressStatus=" + inProgressStatus +
                ", doneStatus=" + doneStatus +
                '}';
    }
}
