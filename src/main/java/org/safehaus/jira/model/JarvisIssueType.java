package org.safehaus.jira.model;


import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Created by tzhamakeev on 5/25/15.
 */
@XmlRootElement
public class JarvisIssueType
{
    private Long id;
    private String name;


    public JarvisIssueType()
    {
    }


    public JarvisIssueType( final Long id, final String name )
    {
        this.id = id;
        this.name = name;
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


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "name", name ).toString();
    }
}
