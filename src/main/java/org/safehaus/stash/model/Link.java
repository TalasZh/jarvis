package org.safehaus.stash.model;


import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Link
{
    @Column
    private String url;

    @Column
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
