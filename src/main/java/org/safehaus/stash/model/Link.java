package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class Link
{
    private String url;
    private String rel;


    public String getUrl()
    {
        return url;
    }


    public String getRel()
    {
        return rel;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "url", url ).add( "rel", rel ).toString();
    }
}
