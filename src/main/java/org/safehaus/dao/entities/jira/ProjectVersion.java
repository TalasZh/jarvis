package org.safehaus.dao.entities.jira;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by talas on 10/3/15.
 */
@Entity
@Table( name = "project_version", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "name" )
} )
public class ProjectVersion implements Serializable
{
    @Id
    @Column( name = "version_id" )
    private String id;

    @Column( name = "description" )
    private String description;

    @Column( name = "version_name" )
    private String name;

    @Column( name = "release_date" )
    private String releaseDate;


    public ProjectVersion()
    {
    }


    public ProjectVersion( final String id, final String description, final String name, final String releaseDate )
    {
        this.id = id;
        this.description = description;
        this.name = name;
        this.releaseDate = releaseDate;
    }


    public String getId()
    {
        return id;
    }


    public void setId( final String id )
    {
        this.id = id;
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


    public String getReleaseDate()
    {
        return releaseDate;
    }


    public void setReleaseDate( final String releaseDate )
    {
        this.releaseDate = releaseDate;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof ProjectVersion ) )
        {
            return false;
        }

        final ProjectVersion that = ( ProjectVersion ) o;

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
        return "ProjectVersion{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
