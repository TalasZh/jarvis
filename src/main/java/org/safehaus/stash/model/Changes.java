package org.safehaus.stash.model;


import java.util.Set;

import com.google.common.base.Objects;


public class Changes
{
    private Set<Commit> changesets;
    private Set<Commit> commits;
    private long total;


    public Set<Commit> getChangesets()
    {
        return changesets;
    }


    public Set<Commit> getCommits()
    {
        return commits;
    }


    public long getTotal()
    {
        return total;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "changesets", changesets ).add( "commits", commits )
                      .add( "total", total ).toString();
    }
}
