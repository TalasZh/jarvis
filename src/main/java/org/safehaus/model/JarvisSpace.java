package org.safehaus.model;


import javax.xml.bind.annotation.XmlRootElement;


/**
 * Created by tzhamakeev on 5/29/15.
 */
@XmlRootElement
public class JarvisSpace
{
    private Long id;
    private String key;
    private String name;
    private String type;


    public JarvisSpace( final Long id, final String key, final String name, final String type )
    {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
    }


    public Long getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public String getKey()
    {
        return key;
    }


    public String getType()
    {
        return type;
    }
}
