package org.safehaus.dao.entities.jira;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

import static org.safehaus.Constants.DATABASE_SCHEMA;

/**
 * Created by tzhamakeev on 5/22/15.
 */
//@XmlRootElement
@Entity
@Table( name = "jarvis_link", schema = DATABASE_SCHEMA )
@Access( AccessType.FIELD )
public class JarvisLink implements Serializable
{
    public enum Direction
    {
        INWARD, OUTWARD
    }

    @Id
    @Column( name = "link_id" )
    private String id;

    @Embedded
    private LinkType linkType;

    @Embedded
    private LinkDirection linkDirection;


    @Column( name = "link_direction" )
    @Enumerated( EnumType.STRING )
    private JarvisLink.Direction direction;


    @Embedded
    private JarvisIssueType type;


    public JarvisLink()
    {
    }


    public JarvisLink( final Long id, final LinkType linkType, final LinkDirection linkDirection,
                       final JarvisIssueType type, Direction direction )
    {
        //        this.id = id;
        this.id = String.format( "%d-%s", id, linkDirection.getIssueId() );
        this.linkType = linkType;
        this.linkDirection = linkDirection;
        this.type = type;
        this.direction = direction;
    }

    public LinkType getLinkType()
    {
        return linkType;
    }


    public JarvisIssueType getType()
    {
        return type;
    }


    public LinkDirection getLinkDirection()
    {
        return linkDirection;
    }


    public Direction getDirection()
    {
        return direction;
    }


    public void setDirection( final Direction direction )
    {
        this.direction = direction;
    }


    //    public JarvisLinkPk getLinkPk()
    //    {
    //        return linkPk;
    //    }
    //
    //
    //    public void setLinkPk( final JarvisLinkPk linkPk )
    //    {
    //        this.linkPk = linkPk;
    //    }


    public void setLinkType( final LinkType linkType )
    {
        this.linkType = linkType;
    }


    public void setLinkDirection( final LinkDirection linkDirection )
    {
        this.linkDirection = linkDirection;
    }


    public void setType( final JarvisIssueType type )
    {
        this.type = type;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JarvisLink ) )
        {
            return false;
        }

        final JarvisLink that = ( JarvisLink ) o;

        return !( id != null ? !id.equals( that.id ) : that.id != null );
    }


    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "JarvisLink{" +
                "id='" + id + '\'' +
                ", linkType=" + linkType +
                ", linkDirection=" + linkDirection +
                ", direction=" + direction +
                ", type=" + type +
                '}';
    }
}
