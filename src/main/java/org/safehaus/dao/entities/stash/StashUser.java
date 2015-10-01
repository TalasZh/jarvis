package org.safehaus.dao.entities.stash;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.safehaus.stash.model.UserType;

import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;

@Entity
@Table( name = "stash_user", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "name" ), @Index( name = "emailAddress" ), @Index( name = "authorTimestamp" )} )
public class StashUser implements Serializable
{
    @Column(name = "stash_user_name")
    private String name;

    @Id
    @Column(name = "user_id")
    private long id;

    @Column(name = "stash_user_dispname")
    private String displayName;

    @Column(name = "stash_user_email")
    private String emailAddress;

    @Column(name = "stash_user_active")
    private boolean active;

    @Column(name = "stash_user_slug")
    private String slug;

    @Enumerated( EnumType.ORDINAL )
    @Column(name = "stash_user_type")
    private UserType type;

    @Embedded
    private Link link;
/*
    // GSON library is not compatible with HibernateSetMap. Makes getCommits useless. Link to user is not a useful field ATM.
    // Commented out for a future generic solution.
    @OneToMany(fetch = FetchType.EAGER)
    @Column(name = "stash_user_links")
    private Map<String, HibernateSetMap> links;
*/
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
/*
    public Map<String, HibernateSetMap> getLinks()
    {
        return links;
    }

    public void setLinks(Map<String, HibernateSetMap> links)
    {
        this.links = links;
    }
*/

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
                      .add( "type", type ).add( "link", link )/*.add( "links", links )*/.toString();
    }
}
