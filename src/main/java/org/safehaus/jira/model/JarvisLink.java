package org.safehaus.jira.model;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Created by tzhamakeev on 5/22/15.
 */
@XmlRootElement
//@Embeddable
@Entity
@Table(schema = "jarvis@cassandra-pu" )
public class JarvisLink
{
    //@Column(name = "JARVIS_LINK_ID")
    @Id
    private Long id;

    @Column(name = "JARVIS_LINK_KEY")
    private String key;

    @Column(name = "JARVIS_LINK_TYPE")
    private String linkType;


    @Column(name = "JARVIS_LINK_DIRECTION")
    private String linkDirection;

    @Column(name = "JARVIS_LINK_ATYPE")
    private JarvisIssueType type;


    public JarvisLink()
    {
    }


    public JarvisLink( final Long id, final String key, final String linkType, final String linkDirection,
                       final JarvisIssueType type )
    {
        this.id = id;
        this.key = key;
        this.linkType = linkType;
        this.linkDirection = linkDirection;
        this.type = type;
    }


    public Long getId()
    {
        return id;
    }


    public String getKey()
    {
        return key;
    }


    public String getLinkType()
    {
        return linkType;
    }


    public JarvisIssueType getType()
    {
        return type;
    }


    public String getLinkDirection()
    {
        return linkDirection;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "key", key ).append( "linkType", linkType )
                                          .append( "linkDirection", linkDirection ).append( "type", type ).toString();
    }
}
