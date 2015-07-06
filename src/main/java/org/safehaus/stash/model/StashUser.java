package org.safehaus.stash.model;


import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.*;

@Entity
@Table( name = "STASH_USER", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "name" ), @Index( name = "emailAddress" ), @Index( name = "authorTimestamp" )} )
public class StashUser
{
    @Column(name = "STASH_USER_NAME")
    private String name;

    @Id
    private long id;

    @Column(name = "STASH_USER_DISPNAME")
    private String displayName;

    @Column(name = "STASH_USER_EMAIL")
    private String emailAddress;

    @Column(name = "STASH_USER_ISACTIVE")
    private boolean active;

    @Column(name = "STASH_USER_SLUG")
    private String slug;

    @Enumerated( EnumType.ORDINAL )
    @Column(name = "STASH_USER_TYPE")
    private UserType type;

    @Embedded
    @Column(name = "STASH_USER_LINK")
    private Link link;

    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "STASH_USER_LINKS")
    private Map<String, HibernateSetMap> links;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public boolean getActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getSlug()
    {
        return slug;
    }

    public void setSlug(String slug)
    {
        this.slug = slug;
    }

    public UserType getType()
    {
        return type;
    }

    public void setType(UserType type)
    {
        this.type = type;
    }

    public Link getLink()
    {
        return link;
    }

    public void setLink(Link link)
    {
        this.link = link;
    }

    public Map<String, HibernateSetMap> getLinks()
    {
        return links;
    }

    public void setLinks(Map<String, HibernateSetMap> links)
    {
        this.links = links;
    }


    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "name", name ).add( "id", id ).add( "displayName", displayName )
                      .add( "emailAddress", emailAddress ).add( "active", active ).add( "slug", slug )
                      .add( "type", type ).add( "link", link ).add( "links", links ).toString();
    }
}
