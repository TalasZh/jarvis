package org.safehaus.stash.model;


import java.util.Set;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;


public class ChangeSet
{
    private String id;
    private String displayId;
    private Author author;
    private long authorTimestamp;
    private String message;
    private Set<ChangeSet> parents;
    private Attribute attributes;


    public String getId()
    {
        return id;
    }


    public String getDisplayId()
    {
        return displayId;
    }


    public Author getAuthor()
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


    public Set<ChangeSet> getParents()
    {
        return parents;
    }


    public Attribute getAttributes()
    {
        return attributes;
    }


    private static class Attribute
    {
        @SerializedName( "jira-key" )
        private Set<String> jiraKey;


        public Set<String> getJiraKey()
        {
            return jiraKey;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "jiraKey", jiraKey ).toString();
        }
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "displayId", displayId ).add( "author", author )
                      .add( "authorTimestamp", authorTimestamp ).add( "message", message ).add( "parents", parents )
                      .add( "attributes", attributes ).toString();
    }
}
