package org.safehaus.dao.entities.jira;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


/**
 * Created by talas on 9/26/15.
 */
@Embeddable
public class LinkDirection
{
    public enum Direction
    {
        INWARD, OUTWARD
    }


    @Column( name = "link_direction" )
    @Enumerated( EnumType.STRING )
    private Direction direction;

    @Column( name = "issue_key" )
    private String issueKey;

    @Column( name = "issue_id" )
    private Long issueId;


    public LinkDirection()
    {
    }


    public LinkDirection( final Direction direction, final String issueKey, final Long issueId )
    {
        this.direction = direction;
        this.issueKey = issueKey;
        this.issueId = issueId;
    }


    public Direction getDirection()
    {
        return direction;
    }


    public void setDirection( final Direction direction )
    {
        this.direction = direction;
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
                "direction='" + direction + '\'' +
                ", issueKey='" + issueKey + '\'' +
                ", issueId=" + issueId +
                '}';
    }
}
