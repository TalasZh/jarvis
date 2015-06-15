package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class MinimalCommit
{
    private String id;
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
