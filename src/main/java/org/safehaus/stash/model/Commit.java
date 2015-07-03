package org.safehaus.stash.model;


import java.util.Set;

import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.*;

public class Commit
{
    private String id;

    private String displayId;

    private StashUser author;

    private long authorTimestamp;

    private String message;

    private Set<MinimalCommit> parents;

    public String getId()
    {
        return id;
    }

    public String getDisplayId()
    {
        return displayId;
    }

    public StashUser getAuthor()
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


    public Set<MinimalCommit> getParents()
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
