package org.safehaus.model;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;


@XmlRootElement
public class JarvisMember extends BaseObject implements Serializable
{
    private static final long serialVersionUID = 3832626162173359411L;

    private String name;
    private String avatar;
    private String displayName;


    /**
     * Default constructor - creates a new instance with no values set.
     */
    public JarvisMember()
    {
    }


    /**
     * Create a new instance and set the name.
     *
     * @param name login name for user.
     */
    public JarvisMember( final String name, final String avatar, final String displayName )
    {
        this.name = name;
        this.avatar = avatar;
        this.displayName = displayName;
    }


    public String getName()
    {
        return name;
    }


    /**
     * {@inheritDoc}
     */
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JarvisMember ) )
        {
            return false;
        }

        final JarvisMember member = ( JarvisMember ) o;

        return !( name != null ? !name.equals( member.getName() ) : member.getName() != null );
    }


    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return ( name != null ? name.hashCode() : 0 );
    }


    public String getAvatar()
    {
        return avatar;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "name", name ).toString();
    }
}
