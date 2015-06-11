package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class Author
{
    private String name;
    private String emailAddress;


    public String getName()
    {
        return name;
    }


    public String getEmailAddress()
    {
        return emailAddress;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "name", name ).add( "emailAddress", emailAddress ).toString();
    }
}
