package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class ReviewCoverage
{
    private Set<TimeValue> allRevisions;
    private Set<TimeValue> coveredRevisions;


    public Set<TimeValue> getAllRevisions()
    {
        return allRevisions;
    }


    public Set<TimeValue> getCoveredRevisions()
    {
        return coveredRevisions;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "allRevisions", allRevisions )
                      .add( "coveredRevisions", coveredRevisions ).toString();
    }
}
