package org.safehaus.analysis;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 9/9/15.
 */
@Embeddable
public class ItemCompoundKey implements Serializable
{
    @Column( name = "issue_id" )
    private Long issueId;

    @Column( name = "issue_key" )
    private String issueKey;

    @Column( name = "change_item_id" )
    String changeItemId;


    public ItemCompoundKey()
    {
    }


    public ItemCompoundKey( final Long issueId, final String issueKey, final String changeItemId )
    {
        this.issueId = issueId;
        this.issueKey = issueKey;
        this.changeItemId = changeItemId;
    }


    public Long getIssueId()
    {
        return issueId;
    }


    public void setIssueId( final Long issueId )
    {
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


    public String getChangeItemId()
    {
        return changeItemId;
    }


    public void setChangeItemId( final String changeItemId )
    {
        this.changeItemId = changeItemId;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof ItemCompoundKey ) )
        {
            return false;
        }

        final ItemCompoundKey that = ( ItemCompoundKey ) o;

        return !( changeItemId != null ? !changeItemId.equals( that.changeItemId ) : that.changeItemId != null );
    }


    @Override
    public int hashCode()
    {
        return changeItemId != null ? changeItemId.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "ItemCompoundKey{" +
                "issueId=" + issueId +
                ", issueKey='" + issueKey + '\'' +
                ", changeItemId='" + changeItemId + '\'' +
                '}';
    }
}
