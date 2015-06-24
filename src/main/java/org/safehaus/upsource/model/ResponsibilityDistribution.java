package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class ResponsibilityDistribution
{
    private Set<CommitterModuleValue> items;
    private Set<CommitterUserInfo> users;
    private Set<String> modules;
    private AnalyzerStats stats;


    public Set<CommitterModuleValue> getItems()
    {
        return items;
    }


    public Set<CommitterUserInfo> getUsers()
    {
        return users;
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
        return Objects.toStringHelper( this ).add( "items", items ).add( "users", users ).add( "modules", modules )
                      .add( "stats", stats ).toString();
    }
}
