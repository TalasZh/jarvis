package org.safehaus.dao.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 10/3/15.
 */
@Embeddable
public class ServiceIdentity implements Serializable
{
    @Column( name = "project_id" )
    private Long projectId;

    @Column( name = "project_key" )
    private String projectKey;


    public ServiceIdentity()
    {
    }


    public ServiceIdentity( final Long projectId, final String projectKey )
    {
        this.projectId = projectId;
        this.projectKey = projectKey;
    }


    public Long getProjectId()
    {
        return projectId;
    }


    public void setProjectId( final Long projectId )
    {
        this.projectId = projectId;
    }


    public String getProjectKey()
    {
        return projectKey;
    }


    public void setProjectKey( final String projectKey )
    {
        this.projectKey = projectKey;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof ServiceIdentity ) )
        {
            return false;
        }

        final ServiceIdentity that = ( ServiceIdentity ) o;

        if ( projectId != null ? !projectId.equals( that.projectId ) : that.projectId != null )
        {
            return false;
        }
        return !( projectKey != null ? !projectKey.equals( that.projectKey ) : that.projectKey != null );
    }


    @Override
    public int hashCode()
    {
        int result = projectId != null ? projectId.hashCode() : 0;
        result = 31 * result + ( projectKey != null ? projectKey.hashCode() : 0 );
        return result;
    }


    @Override
    public String toString()
    {
        return "ServiceIdentity{" +
                "projectId=" + projectId +
                ", projectKey='" + projectKey + '\'' +
                '}';
    }
}
