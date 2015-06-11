package org.safehaus.stash.model;


import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;


public class User
{
    private String name;
    private long id;
    private String displayName;
    private String emailAddress;
    private boolean active;
    private String slug;
    private String type;
    private Link link;
    private Map<String, Set<Map<String, String>>> links;


    public String getName()
    {
        return name;
    }


    public long getId()
    {
        return id;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public boolean isActive()
    {
        return active;
    }


    public String getSlug()
    {
        return slug;
    }


    public String getType()
    {
        return type;
    }


    public Link getLink()
    {
        return link;
    }


    public Map<String, Set<Map<String, String>>> getLinks()
    {
        return links;
    }


    public String getEmailAddress()
    {
        return emailAddress;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "name", name ).add( "id", id ).add( "displayName", displayName )
                      .add( "emailAddress", emailAddress ).add( "active", active ).add( "slug", slug )
                      .add( "type", type ).add( "link", link ).add( "links", links ).toString();
    }
}
