package org.safehaus.stash.model;


import java.util.Date;

import com.google.common.base.Objects;


public class BuildStatus
{
    private State state;
    private String key;
    private String name;
    private String url;
    private String description;
    private long dateAdded;


    public enum State
    {
        SUCCESSFUL, FAILED, INPROGRESS
    }


    public State getState()
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


    public Date getDateAdded()
    {
        return new Date( dateAdded );
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "state", state ).add( "key", key ).add( "name", name )
                      .add( "url", url ).add( "description", description ).add( "dateAdded", getDateAdded() )
                      .toString();
    }
}
