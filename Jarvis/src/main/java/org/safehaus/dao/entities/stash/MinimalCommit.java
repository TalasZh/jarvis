package org.safehaus.dao.entities.stash;


import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;

@Embeddable
public class MinimalCommit
{
    @Column(name = "id")
    private String id;

    @Column(name = "display_id")
    private String displayId;

    public String getId()
    {
        return id;
    }

    public String getDisplayId()
    {
        return displayId;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "displayId", displayId ).toString();
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof MinimalCommit ) )
        {
            return false;
        }

        final MinimalCommit that = ( MinimalCommit ) o;

        return id.equals( that.id ) && displayId.equals( that.displayId );
    }


    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + displayId.hashCode();
        return result;
    }
}
