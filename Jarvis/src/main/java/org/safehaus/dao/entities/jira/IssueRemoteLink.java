package org.safehaus.dao.entities.jira;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by talas on 10/4/15.
 */
@Entity
@Table( name = "issue_remote_link", schema = DATABASE_SCHEMA )
public class IssueRemoteLink implements Serializable
{
    @Id
    @Column( name = "link_id" )
    private String id;

    @Column( name = "link_title" )
    private String title;

    @Column( name = "remote_link" )
    private String remoteUrl;

    @Column( name = "link_url" )
    private String url;


    public IssueRemoteLink()
    {
    }


    public IssueRemoteLink( final String title, final String remoteUrl, final String id, final String url )
    {
        this.title = title;
        this.remoteUrl = remoteUrl;
        this.id = id;
        this.url = url;
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle( final String title )
    {
        this.title = title;
    }


    public String getRemoteUrl()
    {
        return remoteUrl;
    }


    public void setRemoteUrl( final String remoteUrl )
    {
        this.remoteUrl = remoteUrl;
    }


    public String getId()
    {
        return id;
    }


    public void setId( final String id )
    {
        this.id = id;
    }


    public String getUrl()
    {
        return url;
    }


    public void setUrl( final String url )
    {
        this.url = url;
    }


    @Override
    public String toString()
    {
        return "IssueRemoteLink{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
