package org.safehaus.dao.entities.jira;


import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 9/26/15.
 */
@Embeddable
public class LinkType
{
    @Column( name = "link_type_id" )
    private Long id;

    @Column( name = "link_type_name" )
    private String name;

    @Column( name = "inward" )
    private String inward;

    @Column( name = "outward" )
    private String outward;


    public LinkType()
    {
    }


    public LinkType( final Long id, final String name, final String inward, final String outward )
    {
        this.id = id;
        this.name = name;
        this.inward = inward;
        this.outward = outward;
    }


    public Long getId()
    {
        return id;
    }


    public void setId( final Long id )
    {
        this.id = id;
    }


    public String getName()
    {
        return name;
    }


    public void setName( final String name )
    {
        this.name = name;
    }


    public String getInward()
    {
        return inward;
    }


    public void setInward( final String inward )
    {
        this.inward = inward;
    }


    public String getOutward()
    {
        return outward;
    }


    public void setOutward( final String outward )
    {
        this.outward = outward;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof LinkType ) )
        {
            return false;
        }

        final LinkType linkType = ( LinkType ) o;

        return !( id != null ? !id.equals( linkType.id ) : linkType.id != null );
    }


    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "LinkType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inward='" + inward + '\'' +
                ", outward='" + outward + '\'' +
                '}';
    }
}
