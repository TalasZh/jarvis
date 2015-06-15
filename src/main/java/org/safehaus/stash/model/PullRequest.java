package org.safehaus.stash.model;


import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;


public class PullRequest
{

    private long id;
    private long version;
    private String title;
    private String description;
    private PullRequestState state;
    private boolean open;
    private boolean closed;
    private boolean locked;
    private long createdDate;
    private long updatedDate;
    private PullRequestRef fromRef;
    private PullRequestRef toRef;
    private PullRequestParticipant author;
    private Set<PullRequestParticipant> reviewers;
    private Set<PullRequestParticipant> participants;
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


    public PullRequestState getState()
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


    public Date getCreatedDate()
    {
        return new Date( createdDate );
    }


    public Date getUpdatedDate()
    {
        return new Date( updatedDate );
    }


    public PullRequestRef getFromRef()
    {
        return fromRef;
    }


    public PullRequestRef getToRef()
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


    public PullRequestParticipant getAuthor()
    {
        return author;
    }


    public Set<PullRequestParticipant> getReviewers()
    {
        return reviewers;
    }


    public Set<PullRequestParticipant> getParticipants()
    {
        return participants;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "version", version ).add( "title", title )
                      .add( "description", description ).add( "state", state ).add( "open", open )
                      .add( "closed", closed ).add( "locked", locked ).add( "createdDate", getCreatedDate() )
                      .add( "updatedDate", getUpdatedDate() ).add( "fromRef", fromRef ).add( "toRef", toRef )
                      .add( "author", author ).add( "reviewers", reviewers ).add( "participants", participants )
                      .add( "link", link ).add( "links", links ).toString();
    }
}
