package org.safehaus.dao.entities.jira;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 9/9/15.
 */
@Embeddable
public class ChangeCompositeKey implements Serializable
{
    //    @Embedded
    //    ItemCompoundKey itemKey;

    @Column( name = "change_item_id" )
    String changeItemId;

    @Column( name = "created" )
    private Long created;


    public ChangeCompositeKey( String changeItemId, final Long created )
    {
        //        this.itemKey = new ItemCompoundKey( issueId, issueKey, UUID.randomUUID().toString() );
        //        this.issueId = issueId;
        //        this.issueKey = issueKey;
        this.changeItemId = changeItemId;
        this.created = created;
    }


    public ChangeCompositeKey()
    {
    }


    public Long getCreated()
    {
        return created;
    }


    public void setCreated( final Long created )
    {
        this.created = created;
    }


    //    public String getIssueKey()
    //    {
    //        return itemKey.getIssueKey();
    //    }
    //
    //
    //    public void setIssueKey( final String issueKey )
    //    {
    //        this.itemKey.setIssueKey( issueKey );
    //    }
    //
    //
    //    public String getChangeItemId()
    //    {
    //        return itemKey.getChangeItemId();
    //    }
    //
    //
    //    public void setChangeItemId( final String changeItemId )
    //    {
    //        this.itemKey.setChangeItemId( changeItemId );
    //    }


    public String getChangeItemId()
    {
        return changeItemId;
    }


    public void setChangeItemId( final String changeItemId )
    {
        this.changeItemId = changeItemId;
    }

    //
    //    public Long getIssueId()
    //    {
    //        return issueId;
    //    }
    //
    //
    //    public void setIssueId( final Long issueId )
    //    {
    //        this.issueId = issueId;
    //    }
    //
    //
    //    public String getIssueKey()
    //    {
    //        return issueKey;
    //    }
    //
    //
    //    public void setIssueKey( final String issueKey )
    //    {
    //        this.issueKey = issueKey;
    //    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof ChangeCompositeKey ) )
        {
            return false;
        }

        final ChangeCompositeKey that = ( ChangeCompositeKey ) o;

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
        return "ChangeCompositeKey{" +
                "changeItemId='" + changeItemId + '\'' +
                //                ", issueId=" + issueId +
                //                ", issueKey='" + issueKey + '\'' +
                ", created=" + created +
                '}';
    }
}
