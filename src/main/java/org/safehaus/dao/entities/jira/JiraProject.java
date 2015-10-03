package org.safehaus.dao.entities.jira;


import java.io.Serializable;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.collect.Lists;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by talas on 9/27/15.
 */
@Entity
@Table( name = "jira_project", schema = DATABASE_SCHEMA )
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

    @ElementCollection
    @CollectionTable( name = "project_version" )
    private List<ProjectVersion> projectVersions = Lists.newArrayList();


    public JiraProject()
    {
    }


    public JiraProject( final String projectId, final String key, final String assigneeType, final String description,
                        final String name, final List<ProjectVersion> projectVersions )
    {
        this.projectId = projectId;
        this.key = key;
        this.assigneeType = assigneeType;
        this.description = description;
        this.name = name;
        this.projectVersions = projectVersions;
    }


    public List<ProjectVersion> getProjectVersions()
    {
        return projectVersions;
    }


    public void setProjectVersions( final List<ProjectVersion> projectVersions )
    {
        this.projectVersions = projectVersions;
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
                ", projectVersions=" + projectVersions +
                '}';
    }
}
