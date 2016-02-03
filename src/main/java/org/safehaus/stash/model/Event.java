package org.safehaus.stash.model;


import org.safehaus.dao.entities.stash.StashUser;

import com.google.common.base.Objects;


public class Event
{
    private StashUser user;
    private String action;
    private long timestamp;
    private String details;


    public StashUser getUser()
    {
        return user;
    }


    public String getAction()
    {
        return action;
    }


    public long getTimestamp()
    {
        return timestamp;
    }


    public String getDetails()
    {
        return details;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "user", user ).add( "action", action ).add( "timestamp", timestamp )
                      .add( "details", details ).toString();
    }
}
