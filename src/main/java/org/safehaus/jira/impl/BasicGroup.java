package org.safehaus.jira.impl;


import java.net.URI;

import com.atlassian.jira.rest.client.api.AddressableEntity;
import com.atlassian.jira.rest.client.api.NamedEntity;
import com.atlassian.util.concurrent.Nullable;
import com.google.common.base.Objects;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public class BasicGroup implements AddressableEntity, NamedEntity
{
    private final URI self;

    @Nullable
    private final Long id;
    @Nullable
    private final String name;


    public BasicGroup( URI self, @Nullable Long id, @Nullable String name )
    {
        this.self = self;
        this.id = id;
        this.name = name;
    }


    public Long getId()
    {
        return id;
    }


    @Override
    public URI getSelf()
    {
        return self;
    }


    @Override
    public String getName()
    {
        return name;
    }


    public String toString()
    {
        return this.getToStringHelper().toString();
    }


    protected Objects.ToStringHelper getToStringHelper()
    {
        return Objects.toStringHelper( this ).add( "self", this.self ).add( "id", this.id ).add( "name", this.name );
    }


    public boolean equals( Object obj )
    {
        if ( !( obj instanceof BasicGroup ) )
        {
            return false;
        }
        else
        {
            BasicGroup that = ( BasicGroup ) obj;
            return Objects.equal( this.self, that.self ) && Objects.equal( this.name, that.name ) && Objects
                    .equal( this.id, that.id );
        }
    }


    public int hashCode()
    {
        return Objects.hashCode( new Object[] { this.self, this.name, this.id } );
    }
}
