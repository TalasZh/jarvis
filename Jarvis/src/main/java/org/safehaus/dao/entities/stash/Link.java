package org.safehaus.dao.entities.stash;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.common.base.Objects;

@Embeddable
public class Link implements Serializable
{
    @Column(name = "url")
    private String url;

    @Column(name = "rel")
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


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof Link ) )
        {
            return false;
        }

        final Link link = ( Link ) o;

        return url.equals( link.url ) && rel.equals( link.rel );
    }


    @Override
    public int hashCode()
    {
        int result = url.hashCode();
        result = 31 * result + rel.hashCode();
        return result;
    }
}
