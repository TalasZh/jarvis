package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class ReviewList
{
    private Set<ReviewDescriptor> reviews;
    private boolean more;


    public Set<ReviewDescriptor> getReviews()
    {
        return reviews;
    }


    public boolean isMore()
    {
        return more;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "reviews", reviews ).add( "more", more ).toString();
    }
}
