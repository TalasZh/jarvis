package org.safehaus.stash.model;


import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
}
