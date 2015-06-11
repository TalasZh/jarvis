package org.safehaus.stash.model;


import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;


public class PullRequest
{

    private long id;
    private long version;
    private String title;
    private String description;
    private String state;
    private boolean open;
    private boolean closed;
    private boolean locked;
    private long createdDate;
    private long updatedDate;
    private Ref fromRef;
    private Ref toRef;
    private Member author;
    private Set<Member> reviewers;
    private Set<Member> participants;
    private Link link;
    private Map<String, Set<Map<String, String>>> links;


    public long getId()
    {
        return id;
    }


    public long getVersion()
    {
        return version;
    }


    public String getTitle()
    {
        return title;
    }


    public String getDescription()
    {
        return description;
    }


    public String getState()
    {
        return state;
    }


    public boolean isOpen()
    {
        return open;
    }


    public boolean isClosed()
    {
        return closed;
    }


    public boolean isLocked()
    {
        return locked;
    }


    public long getCreatedDate()
    {
        return createdDate;
    }


    public long getUpdatedDate()
    {
        return updatedDate;
    }


    public Ref getFromRef()
    {
        return fromRef;
    }


    public Ref getToRef()
    {
        return toRef;
    }


    public Link getLink()
    {
        return link;
    }


    public Map<String, Set<Map<String, String>>> getLinks()
    {
        return links;
    }


    public Member getAuthor()
    {
        return author;
    }


    public Set<Member> getReviewers()
    {
        return reviewers;
    }


    public Set<Member> getParticipants()
    {
        return participants;
    }


    public enum State
    {
        ALL, MERGED, OPEN, DECLINED
    }


    public static class Ref
    {
        private String id;
        private String displayId;
        private String latestChangeset;
        private Repo repository;


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


        public Repo getRepository()
        {
            return repository;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "id", id ).add( "displayId", displayId )
                          .add( "latestChangeset", latestChangeset ).add( "repository", repository ).toString();
        }
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "version", version ).add( "title", title )
                      .add( "description", description ).add( "state", state ).add( "open", open )
                      .add( "closed", closed ).add( "locked", locked ).add( "createdDate", createdDate )
                      .add( "updatedDate", updatedDate ).add( "fromRef", fromRef ).add( "toRef", toRef )
                      .add( "author", author ).add( "reviewers", reviewers ).add( "participants", participants )
                      .add( "link", link ).add( "links", links ).toString();
    }
}
