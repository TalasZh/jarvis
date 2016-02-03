package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class JiraIssue
{
    private String key;
    private String url;


    public String getKey()
    {
        return key;
    }


    public String getUrl()
    {
        return url;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "key", key ).add( "url", url ).toString();
    }
}
