package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class BuildStatus
{
    private String state;
    private String key;
    private String name;
    private String url;
    private String description;
    private long dateAdded;


    public String getState()
    {
        return state;
    }


    public String getKey()
    {
        return key;
    }


    public String getName()
    {
        return name;
    }


    public String getUrl()
    {
        return url;
    }


    public String getDescription()
    {
        return description;
    }


    public long getDateAdded()
    {
        return dateAdded;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "state", state ).add( "key", key ).add( "name", name )
                      .add( "url", url ).add( "description", description ).add( "dateAdded", dateAdded ).toString();
    }
}
