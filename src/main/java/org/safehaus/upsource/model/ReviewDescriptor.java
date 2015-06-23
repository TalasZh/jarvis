package org.safehaus.upsource.model;


import java.util.Set;


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
}
