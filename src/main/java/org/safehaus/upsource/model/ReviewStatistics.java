package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class ReviewStatistics
{
    private int openReviews;
    private int closedReviews;
    private int allRevisions;
    private int revisionsCoveredByOpenReviews;
    private int revisionsCoveredByClosedReviews;

    private Set<UserValue> authorStatsByReviews;
    private Set<UserValue> reviewerStatsByReviews;
    private Set<UserValue> authorStatsByRevisions;
    private Set<UserValue> reviewerStatsByRevisions;


    public int getOpenReviews()
    {
        return openReviews;
    }


    public int getClosedReviews()
    {
        return closedReviews;
    }


    public int getAllRevisions()
    {
        return allRevisions;
    }


    public int getRevisionsCoveredByOpenReviews()
    {
        return revisionsCoveredByOpenReviews;
    }


    public int getRevisionsCoveredByClosedReviews()
    {
        return revisionsCoveredByClosedReviews;
    }


    public Set<UserValue> getAuthorStatsByReviews()
    {
        return authorStatsByReviews;
    }


    public Set<UserValue> getReviewerStatsByReviews()
    {
        return reviewerStatsByReviews;
    }


    public Set<UserValue> getAuthorStatsByRevisions()
    {
        return authorStatsByRevisions;
    }


    public Set<UserValue> getReviewerStatsByRevisions()
    {
        return reviewerStatsByRevisions;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "openReviews", openReviews ).add( "closedReviews", closedReviews )
                      .add( "allRevisions", allRevisions )
                      .add( "revisionsCoveredByOpenReviews", revisionsCoveredByOpenReviews )
                      .add( "revisionsCoveredByClosedReviews", revisionsCoveredByClosedReviews )
                      .add( "authorStatsByReviews", authorStatsByReviews )
                      .add( "reviewerStatsByReviews", reviewerStatsByReviews )
                      .add( "authorStatsByRevisions", authorStatsByRevisions )
                      .add( "reviewerStatsByRevisions", reviewerStatsByRevisions ).toString();
    }
}
