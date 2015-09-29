package org.safehaus.timeline.model;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;


/**
 * Created by talas on 9/27/15.
 */
@XmlRootElement
@Entity
@Access( AccessType.FIELD )
@Table( name = "structured_project", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "id" ), @Index( name = "key" )
} )
public class StructuredProject implements Serializable
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

    @JsonView( Views.TimelineLong.class )
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true )
    @JoinColumn( name = "referenced_project_id" )
    private Set<StructuredIssue> issues = Sets.newHashSet();


    public StructuredProject()
    {
    }


    public StructuredProject( final String id, final String name, final String key )
    {
        this.id = id;
        this.name = name;
        this.key = key;
    }


    public Set<StructuredIssue> getIssues()
    {
        return issues;
    }


    public void setIssues( final Set<StructuredIssue> issues )
    {
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
