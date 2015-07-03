package org.safehaus.stash.model;


import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.*;

//@Entity
//@Table( name = "STASH_USER", schema = "jarvis@cassandra-pu" )
//@IndexCollection( columns = {
//        @Index( name = "name" ), @Index( name = "emailAddress" ), @Index( name = "authorTimestamp" )
//} )
public class StashUser
{
    //@Column(name = "STASH_USER_NAME")
    private String name;

    //@Id
    private long id;

    //@Column(name = "STASH_USER_DISPNAME")
    private String displayName;

    //@Column(name = "STASH_USER_EMAIL")
    private String emailAddress;

    //@Column(name = "STASH_USER_ISACTIVE")
    private boolean active;

    //@Column(name = "STASH_USER_ISACTIVE")
    private String slug;

    //@Enumerated( EnumType.ORDINAL )
    private UserType type;

    //@Embedded
    //@Column(name = "STASH_USER_LINK")
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


    public UserType getType()
    {
        return type;
    }


    public Link getLink()
    {
        return link;
    }

    //@ElementCollection
    //@Column
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
