package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class Event
{
    private User user;
    private String action;
    private long timestamp;
    private String details;


    public User getUser()
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
