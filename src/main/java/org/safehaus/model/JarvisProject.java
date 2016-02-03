package org.safehaus.model;


import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.dao.entities.jira.JarvisMember;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonView;


@XmlRootElement
public class JarvisProject
{
    private static final long serialVersionUID = 3832626162173359411L;

    @JsonView( Views.JarvisProjectShort.class )
    private Long id;
    @JsonView( Views.JarvisProjectShort.class )
    private String key;
    @JsonView( Views.JarvisProjectShort.class )
    private String name;                    // required
    @JsonView( Views.JarvisProjectShort.class )
    private String description;
    @JsonView( Views.JarvisProjectLong.class )
    private List<JarvisMember> teamMembers = Collections.emptyList();
    @JsonView( Views.JarvisProjectLong.class )
    private List<String> issueTypes = Collections.emptyList();


    /**
     * Default constructor - creates a new instance with no values set.
     */
    public JarvisProject()
    {
    }


    /**
     * Create a new instance and set the name.
     *
     * @param name login name for user.
     */
    public JarvisProject( final Long projectId, final String key, final String name, final String description,
                          final List<String> issueTypes )
    {
        this.id = projectId;
        this.name = name;
        this.key = key;
        this.issueTypes = issueTypes;
        this.description = description;
    }


    public Long getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public String getKey()
    {
        return key;
    }


    public void setKey( final String key )
    {
        this.key = key;
    }


    @XmlElement
    public List<JarvisMember> getTeamMembers()
    {
        return teamMembers;
    }


    public void setTeamMembers( final List<JarvisMember> teamMembers )
    {
        this.teamMembers = teamMembers;
    }


    /**
     * {@inheritDoc}
     */
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JarvisProject ) )
        {
            return false;
        }

        final JarvisProject project = ( JarvisProject ) o;

        return !( name != null ? !name.equals( project.getName() ) : project.getName() != null );
    }


    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return ( name != null ? name.hashCode() : 0 );
    }


    public void setId( final Long id )
    {
        this.id = id;
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
}
