package org.safehaus.dao.entities.jira;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Created by talas on 9/27/15.
 */
@Entity
@Access( AccessType.FIELD )
@Table( name = "jira_project", schema = "jarvis@cassandra-pu" )
public class JiraProject implements Serializable
{
    @Id
    @Column( name = "project_id" )
    private Long id;

    @Column( name = "key" )
    private String key;

    @Column( name = "assignee_type" )
    private String assigneeType;

    @Column( name = "description" )
    private String description;

    @Column( name = "name" )
    private String name;


    public JiraProject( final Long id, final String key, final String assigneeType, final String description,
                        final String name )
    {
        this.id = id;
        this.key = key;
        this.assigneeType = assigneeType;
        this.description = description;
        this.name = name;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JiraProject ) )
        {
            return false;
        }

        final JiraProject that = ( JiraProject ) o;

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
        return "JiraProject{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", assigneeType='" + assigneeType + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
