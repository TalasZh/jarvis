package org.safehaus.model;


import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by tzhamakeev on 5/22/15.
 */
@XmlRootElement
public class JarvisLink
{
    private String key;
    private String linkType;
    private String type;


    public JarvisLink()
    {
    }


    public JarvisLink( final String key, final String linkType, final String type )
    {
        this.key = key;
        this.linkType = linkType;
        this.type = type;
    }


    public String getKey()
    {
        return key;
    }


    public String getLinkType()
    {
        return linkType;
    }


    public String getType()
    {
        return type;
    }
}
