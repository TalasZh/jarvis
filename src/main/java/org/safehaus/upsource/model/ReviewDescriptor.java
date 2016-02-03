package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class ReviewDescriptor
{
    private ReviewId reviewId;
    private String title;
    private Set<ParticipantInReview> participants;
    private int state;
    private boolean unread;
    private int priority;
    private String branch;
    private IssueInfo issue;
    private boolean canCreateIssue;


    public ReviewId getReviewId()
    {
        return reviewId;
    }


    public String getTitle()
    {
        return title;
    }


    public Set<ParticipantInReview> getParticipants()
    {
        return participants;
    }


    public int getState()
    {
        return state;
    }


    public boolean isUnread()
    {
        return unread;
    }


    public int getPriority()
    {
        return priority;
    }


    public String getBranch()
    {
        return branch;
    }


    public IssueInfo getIssue()
    {
        return issue;
    }


    public boolean isCanCreateIssue()
    {
        return canCreateIssue;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "reviewId", reviewId ).add( "title", title )
                      .add( "participants", participants ).add( "state", state ).add( "unread", unread )
                      .add( "priority", priority ).add( "branch", branch ).add( "issue", issue )
                      .add( "canCreateIssue", canCreateIssue ).toString();
    }
}
