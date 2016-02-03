package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class Branch
{
    private String id;
    private String displayId;
    private String latestChangeset;
    private String latestCommit;
    private boolean isDefault;


    public String getId()
    {
        return id;
    }


    public String getDisplayId()
    {
        return displayId;
    }


    public String getLatestChangeset()
    {
        return latestChangeset;
    }


    public String getLatestCommit()
    {
        return latestCommit;
    }


    public boolean isDefault()
    {
        return isDefault;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "displayId", displayId )
                      .add( "latestChangeset", latestChangeset ).add( "latestCommit", latestCommit )
                      .add( "isDefault", isDefault ).toString();
    }
}
