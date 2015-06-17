package org.safehaus.jira.model;


import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Created by tzhamakeev on 5/22/15.
 */
@XmlRootElement
public class JarvisLink
{
    private Long id;
    private String key;
    private String linkType;
    private String linkDirection;
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
