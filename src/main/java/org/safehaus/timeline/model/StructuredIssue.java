package org.safehaus.timeline.model;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;


/**
 * Created by talas on 9/27/15.
 */
@Entity
@Access( AccessType.FIELD )
@Table( name = "structured_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "key" )
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
    @Column( name = "created" )
    private Long created;

    @JsonView( Views.TimelineShort.class )
    @Column( name = "status" )
    private String status;

    @JsonView( Views.TimelineLong.class )
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn( name = "parent_issue_id" )
    private Set<StructuredIssue> issues = Sets.newHashSet();

    @Embedded
    private ProgressStatus openStatus;

    @Embedded
    private ProgressStatus inProgressStatus;

    @Embedded
    private ProgressStatus doneStatus;


    public StructuredIssue()
    {
    }


    public StructuredIssue( final String key, final Long id, final String issueType, final String summary,
                            final String reporter, final String creator, final String assignee, final Long updated,
                            final Long created, final String status )
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
    }


    public Set<StructuredIssue> getIssues()
    {
        return issues;
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


    public String getKey()
    {
        return key;
    }


    public String getStatus()
    {
        return status;
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
