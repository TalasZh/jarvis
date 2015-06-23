package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class ReviewId
{
    private String projectId;
    private String reviewId;


    public String getProjectId()
    {
        return projectId;
    }


    public String getReviewId()
    {
        return reviewId;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "projectId", projectId ).add( "reviewId", reviewId ).toString();
    }
}
