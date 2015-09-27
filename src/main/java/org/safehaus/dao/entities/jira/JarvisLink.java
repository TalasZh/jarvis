package org.safehaus.dao.entities.jira;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by tzhamakeev on 5/22/15.
 */
@XmlRootElement
//@Embeddable
@Entity
@Table( name = "jarvis_link", schema = "jarvis@cassandra-pu" )
@Access( AccessType.FIELD )
public class JarvisLink
{

    public enum Direction
    {
        INWARD, OUTWARD
    }


    @Id
    @Column( name = "link_id" )
    private Long id;

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
        this.id = id;
        this.linkType = linkType;
        this.linkDirection = linkDirection;
        this.type = type;
        this.direction = direction;
    }


    public Long getId()
    {
        return id;
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


    @Override
    public String toString()
    {
        return "JarvisLink{" +
                "id=" + id +
                ", linkType=" + linkType +
                ", linkDirection=" + linkDirection +
                ", direction=" + direction +
                ", type=" + type +
                '}';
    }
}
