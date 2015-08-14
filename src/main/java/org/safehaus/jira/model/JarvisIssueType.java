package org.safehaus.jira.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Created by tzhamakeev on 5/25/15.
 */
@XmlRootElement
@Embeddable
public class JarvisIssueType
{
    @Column(name = "jarvis_issue_type_id")
    private Long id;

    @Column(name = "jarvis_issue_type_name")
    private String name;


    public JarvisIssueType()
    {
    }


    public JarvisIssueType( final Long id, final String name )
    {
        this.id = id;
        this.name = name;
    }


    public Long getId()
    {
        return id;
    }


    public void setId( final Long id )
    {
        this.id = id;
    }


    public String getName()
    {
        return name;
    }


    public void setName( final String name )
    {
        this.name = name;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "name", name ).toString();
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JarvisIssueType ) )
        {
            return false;
        }

        final JarvisIssueType that = ( JarvisIssueType ) o;

        return id.equals( that.id ) && name.equals( that.name );
    }


    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
