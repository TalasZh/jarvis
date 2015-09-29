package org.safehaus.timeline;


import java.util.Date;
import java.util.Set;

import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;


/**
 * Created by talas on 9/27/15.
 */
public class StructuredIssue
{
    @JsonView( Views.TimelineShort.class )
    private String key;

    @JsonView( Views.TimelineShort.class )
    private Long id;

    @JsonView( Views.TimelineShort.class )
    private String issueType;

    @JsonView( Views.TimelineShort.class )
    private String summary;

    @JsonView( Views.TimelineShort.class )
    private String reporter;

    @JsonView( Views.TimelineShort.class )
    private String creator;

    @JsonView( Views.TimelineShort.class )
    private String assignee;

    @JsonView( Views.TimelineShort.class )
    private Date updated;

    @JsonView( Views.TimelineLong.class )
    private Set<StructuredIssue> issues = Sets.newHashSet();


    public StructuredIssue( final String key, final Long id, final String issueType, final String summary,
                            final String reporter, final String creator, final String assignee, final Date updated )
    {
        this.key = key;
        this.id = id;
        this.issueType = issueType;
        this.summary = summary;
        this.reporter = reporter;
        this.creator = creator;
        this.assignee = assignee;
        this.updated = updated;
    }


    public Set<StructuredIssue> getIssues()
    {
        return issues;
    }


    public String getKey()
    {
        return key;
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
                "key='" + key + '\'' +
                ", id='" + id + '\'' +
                ", issueType='" + issueType + '\'' +
                ", summary='" + summary + '\'' +
                ", reporter='" + reporter + '\'' +
                ", creator='" + creator + '\'' +
                ", assignee='" + assignee + '\'' +
                ", updated='" + updated + '\'' +
                ", issues=" + issues +
                '}';
    }
}
