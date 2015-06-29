package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class CommitterModuleValue
{
    private String committer;
    private Set<ModuleValue> items;


    public String getCommitter()
    {
        return committer;
    }


    public Set<ModuleValue> getItems()
    {
        return items;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "committer", committer ).add( "items", items ).toString();
    }
}
