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
    Long changeItemId;

    @Column( name = "created" )
    private Long created;


    public ChangeCompositeKey( final Long changeItemId, final Long created )
    {
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


    public Long getChangeItemId()
    {
        return changeItemId;
    }


    public void setChangeItemId( final Long changeItemId )
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
                ", created=" + created +
                '}';
    }
}
