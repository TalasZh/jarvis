package org.safehaus.timeline;


import java.util.List;

import org.codehaus.jackson.map.annotate.JsonView;
import org.safehaus.model.Views;

import com.google.common.collect.Lists;


/**
 * Created by talas on 9/27/15.
 */
public class StructuredIssue
{
    @JsonView( Views.TimelineShort.class )
    private String key;

    @JsonView( Views.TimelineShort.class )
    private String id;

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
    private String updated;

    @JsonView( Views.TimelineLong.class )
    private List<StructuredIssue> issues = Lists.newArrayList();


    public StructuredIssue( final String key, final String id, final String issueType, final String summary,
                            final String reporter, final String creator, final String assignee, final String updated,
                            final List<StructuredIssue> issues )
    {
        this.key = key;
        this.id = id;
        this.issueType = issueType;
        this.summary = summary;
        this.reporter = reporter;
        this.creator = creator;
        this.assignee = assignee;
        this.updated = updated;
        this.issues = issues;
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
