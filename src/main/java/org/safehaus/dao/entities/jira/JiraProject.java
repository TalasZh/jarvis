package org.safehaus.dao.entities.jira;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;


/**
 * Created by talas on 9/27/15.
 */
@Entity
@Table( name = "jira_project", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "projectId" ), @Index( name = "key" )
} )
public class JiraProject implements Serializable
{
    @Id
    @Column( name = "project_id" )
    private String projectId;

    @Column( name = "project_key" )
    private String key;

    @Column( name = "assignee_type" )
    private String assigneeType;

    @Column( name = "description" )
    private String description;

    @Column( name = "name" )
    private String name;


    public JiraProject()
    {
    }


    public JiraProject( final String projectId, final String key, final String assigneeType, final String description,
                        final String name )
    {
        this.projectId = projectId;
        this.key = key;
        this.assigneeType = assigneeType;
        this.description = description;
        this.name = name;
    }


    public String getProjectId()
    {
        return projectId;
    }


    public void setProjectId( final String projectId )
    {
        this.projectId = projectId;
    }


    public String getKey()
    {
        return key;
    }


    public void setKey( final String key )
    {
        this.key = key;
    }


    public String getAssigneeType()
    {
        return assigneeType;
    }


    public void setAssigneeType( final String assigneeType )
    {
        this.assigneeType = assigneeType;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription( final String description )
    {
        this.description = description;
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

        return !( projectId != null ? !projectId.equals( that.projectId ) : that.projectId != null );
    }


    @Override
    public int hashCode()
    {
        return projectId != null ? projectId.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "JiraProject{" +
                "projectId='" + projectId + '\'' +
                ", key='" + key + '\'' +
                ", assigneeType='" + assigneeType + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
