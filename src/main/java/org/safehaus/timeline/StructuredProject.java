package org.safehaus.timeline;


import java.util.List;

import org.codehaus.jackson.map.annotate.JsonView;
import org.safehaus.model.Views;

import com.google.common.collect.Lists;


/**
 * Created by talas on 9/27/15.
 */
public class StructuredProject
{
    @JsonView( Views.TimelineShort.class )
    private String id;

    @JsonView( Views.TimelineShort.class )
    private String name;

    @JsonView( Views.TimelineShort.class )
    private String key;

    @JsonView( Views.TimelineLong.class )
    private List<StructuredIssue> issues = Lists.newArrayList();


    public StructuredProject( final String id, final String name, final String key, final List<StructuredIssue> issues )
    {
        this.id = id;
        this.name = name;
        this.key = key;
        this.issues = issues;
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
                '}';
    }
}
