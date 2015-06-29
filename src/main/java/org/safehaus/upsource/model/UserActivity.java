package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class UserActivity
{
    private Set<TimeValue> items;
    private AnalyzerStats stats;


    public Set<TimeValue> getItems()
    {
        return items;
    }


    public AnalyzerStats getStats()
    {
        return stats;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "items", items ).add( "stats", stats ).toString();
    }
}
