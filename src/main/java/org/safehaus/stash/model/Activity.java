package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class Activity
{
    //TODO actualize with the latest Stash API

    private long id;
    private long createdDate;
    private User user;
    private String action;

    private ChangeSet changeset;


    public long getId()
    {
        return id;
    }


    public long getCreatedDate()
    {
        return createdDate;
    }


    public User getUser()
    {
        return user;
    }


    public String getAction()
    {
        return action;
    }


    public ChangeSet getChangeset()
    {
        return changeset;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "createdDate", createdDate ).add( "user", user )
                      .add( "action", action ).add( "changeset", changeset ).toString();
    }
}
