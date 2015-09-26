package org.safehaus.dao.entities.jira;


import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Created by tzhamakeev on 5/22/15.
 */
@XmlRootElement
//@Embeddable
@Entity
@Table( name = "jarvis_link", schema = "jarvis@cassandra-pu" )
public class JarvisLink
{
    @Id
    @Column( name = "link_id" )
    private Long id;

    @Embedded
    private LinkType linkType;

    @Embedded
    private LinkDirection linkDirection;


    @Embedded
    private JarvisIssueType type;


    public JarvisLink()
    {
    }


    public JarvisLink( final Long id, final LinkType linkType, final LinkDirection linkDirection,
                       final JarvisIssueType type )
    {
        this.id = id;
        this.linkType = linkType;
        this.linkDirection = linkDirection;
        this.type = type;
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


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "linkType", linkType )
                                          .append( "linkDirection", linkDirection ).append( "type", type ).toString();
    }
}
