package org.safehaus.stash.client;


import java.util.Set;

import com.google.common.base.Objects;


public class Page<T>
{
    private int size;
    private int limit;
    private boolean isLastPage;
    private int start;
    private Set<T> values;


    public int getSize()
    {
        return size;
    }


    public int getLimit()
    {
        return limit;
    }


    public boolean isLastPage()
    {
        return isLastPage;
    }


    public int getStart()
    {
        return start;
    }


    public Set<T> getValues()
    {
        return values;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "size", size ).add( "limit", limit ).add( "isLastPage", isLastPage )
                      .add( "start", start ).add( "values", values ).toString();
    }
}
