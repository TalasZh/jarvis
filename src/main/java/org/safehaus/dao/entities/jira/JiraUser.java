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
 * Created by talas on 10/4/15.
 */
@Entity
@Table( name = "jira_user", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "username" )
} )
public class JiraUser implements Serializable
{
    @Id
    @Column( name = "user_id" )
    private String userId;

    @Column( name = "display_name" )
    private String displayName;

    @Column( name = "email" )
    private String email;

    @Column( name = "username" )
    private String username;


    public JiraUser()
    {
    }


    public JiraUser( final String userId, final String displayName, final String email, final String username )
    {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.username = username;
    }


    public String getUserId()
    {
        return userId;
    }


    public void setUserId( final String userId )
    {
        this.userId = userId;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public void setDisplayName( final String displayName )
    {
        this.displayName = displayName;
    }


    public String getEmail()
    {
        return email;
    }


    public void setEmail( final String email )
    {
        this.email = email;
    }


    public String getUsername()
    {
        return username;
    }


    public void setUsername( final String username )
    {
        this.username = username;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JiraUser ) )
        {
            return false;
        }

        final JiraUser jiraUser = ( JiraUser ) o;

        return !( userId != null ? !userId.equals( jiraUser.userId ) : jiraUser.userId != null );
    }


    @Override
    public int hashCode()
    {
        return userId != null ? userId.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "JiraUser{" +
                "username='" + username + '\'' +
                '}';
    }
}
