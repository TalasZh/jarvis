package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class ProjectActivity
{
    private Set<TimeValue> items;
    private Set<String> modules;
    private AnalyzerStats stats;


    public Set<TimeValue> getItems()
    {
        return items;
    }


    public Set<String> getModules()
    {
        return modules;
    }


    public AnalyzerStats getStats()
    {
        return stats;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "items", items ).add( "modules", modules ).add( "stats", stats )
                      .toString();
    }
}
