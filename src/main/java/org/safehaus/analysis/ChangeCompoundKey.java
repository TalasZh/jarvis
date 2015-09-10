package org.safehaus.analysis;


import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;


/**
 * Created by talas on 9/9/15.
 */
@Embeddable
public class ChangeCompoundKey implements Serializable
{
    @Embedded
    ItemCompoundKey itemKey;

    @Column( name = "created" )
    private Long created;


    public ChangeCompoundKey( final Long issueId, final Long created, final String issueKey )
    {
        this.itemKey = new ItemCompoundKey( issueId, issueKey, UUID.randomUUID().toString() );
        this.created = created;
    }


    public ChangeCompoundKey()
    {
    }


    public ItemCompoundKey getItemKey()
    {
        return itemKey;
    }


    public void setItemKey( final ItemCompoundKey itemKey )
    {
        this.itemKey = itemKey;
    }


    public Long getIssueId()
    {
        return itemKey.getIssueId();
    }


    public void setIssueId( final Long issueId )
    {
        this.itemKey.setIssueId( issueId );
    }


    public Long getCreated()
    {
        return created;
    }


    public void setCreated( final Long created )
    {
        this.created = created;
    }


    public String getIssueKey()
    {
        return itemKey.getIssueKey();
    }


    public void setIssueKey( final String issueKey )
    {
        this.itemKey.setIssueKey( issueKey );
    }


    public String getChangeItemId()
    {
        return itemKey.getChangeItemId();
    }


    public void setChangeItemId( final String changeItemId )
    {
        this.itemKey.setChangeItemId( changeItemId );
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof ChangeCompoundKey ) )
        {
            return false;
        }

        final ChangeCompoundKey that = ( ChangeCompoundKey ) o;

        return !( itemKey != null ? !itemKey.equals( that.itemKey ) : that.itemKey != null );
    }


    @Override
    public int hashCode()
    {
        return itemKey != null ? itemKey.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "ChangeCompoundKey{" +
                "itemKey=" + itemKey +
                ", created=" + created +
                '}';
    }
}
