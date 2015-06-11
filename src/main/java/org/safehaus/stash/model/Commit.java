package org.safehaus.stash.model;


import java.util.Set;

import com.google.common.base.Objects;


public class Commit
{
    private String id;
    private String displayId;
    private User author;
    private long authorTimestamp;
    private String message;
    private Set<Commit> parents;


    public String getId()
    {
        return id;
    }


    public String getDisplayId()
    {
        return displayId;
    }


    public User getAuthor()
    {
        return author;
    }


    public long getAuthorTimestamp()
    {
        return authorTimestamp;
    }


    public String getMessage()
    {
        return message;
    }


    public Set<Commit> getParents()
    {
        return parents;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "displayId", displayId ).add( "author", author )
                      .add( "authorTimestamp", authorTimestamp ).add( "message", message ).add( "parents", parents )
                      .toString();
    }
}
