package org.safehaus.stash.model;


import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Link
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
}
