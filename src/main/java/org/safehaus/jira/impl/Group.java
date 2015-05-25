package org.safehaus.jira.impl;


import java.net.URI;
import java.util.Collection;
import java.util.Iterator;

import com.atlassian.jira.rest.client.api.ExpandableResource;
import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.util.concurrent.Nullable;
import com.google.common.base.Objects;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public class Group extends BasicGroup implements ExpandableResource
{

    @Nullable
    private final Iterable<String> expandos;
    @Nullable
    private final Collection<User> users;


    public Group( final Iterable<String> expandos, final URI self, @Nullable final Long id, @Nullable final String name,
                  @Nullable Collection<User> users )
    {
        super( self, id, name );
        this.expandos = expandos;
        this.users = users;
    }


    @Nullable
    public Collection<User> getUsers()
    {
        return users;
    }


    @Override
    public Iterable<String> getExpandos()
    {
        return this.expandos;
    }


    @Override
    protected Objects.ToStringHelper getToStringHelper()
    {
        return super.getToStringHelper().
                add( "users", users ).
                            add( "expandos", expandos );
    }
}
