package org.safehaus.dao.entities.stash;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 9/30/15.
 */
@Embeddable
public class StashMetricPK implements Serializable
{
    @Column( name = "content_id" )
    private String contentId;

    @Column( name = "author_ts" )
    private Long authorTs;


    public StashMetricPK()
    {
    }


    public StashMetricPK( final String contentId, final Long authorTs )
    {
        this.contentId = contentId;
        this.authorTs = authorTs;
    }


    public String getContentId()
    {
        return contentId;
    }


    public void setContentId( final String contentId )
    {
        this.contentId = contentId;
    }


    public Long getAuthorTs()
    {
        return authorTs;
    }


    public void setAuthorTs( final Long author_ts )
    {
        this.authorTs = author_ts;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof StashMetricPK ) )
        {
            return false;
        }

        final StashMetricPK that = ( StashMetricPK ) o;

        return !( contentId != null ? !contentId.equals( that.contentId ) : that.contentId != null );
    }


    @Override
    public int hashCode()
    {
        return contentId != null ? contentId.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "StashMetricPK{" +
                "contentId='" + contentId + '\'' +
                ", author_ts=" + authorTs +
                '}';
    }
}
