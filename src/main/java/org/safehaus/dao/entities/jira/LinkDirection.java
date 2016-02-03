package org.safehaus.dao.entities.jira;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


/**
 * Created by talas on 9/26/15.
 */
@Embeddable
public class LinkDirection implements Serializable
{
    @Column( name = "issue_key" )
    private String issueKey;

    @Column( name = "issue_id" )
    private Long issueId;


    public LinkDirection()
    {
    }


    public LinkDirection( final String issueKey, final Long issueId )
    {
        this.issueKey = issueKey;
        this.issueId = issueId;
    }


    public String getIssueKey()
    {
        return issueKey;
    }


    public void setIssueKey( final String issueKey )
    {
        this.issueKey = issueKey;
    }


    public Long getIssueId()
    {
        return issueId;
    }


    public void setIssueId( final Long issueId )
    {
        this.issueId = issueId;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof LinkDirection ) )
        {
            return false;
        }

        final LinkDirection that = ( LinkDirection ) o;

        return !( issueId != null ? !issueId.equals( that.issueId ) : that.issueId != null );
    }


    @Override
    public int hashCode()
    {
        return issueId != null ? issueId.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "LinkDirection{" +
                ", issueKey='" + issueKey + '\'' +
                ", issueId=" + issueId +
                '}';
    }
}
