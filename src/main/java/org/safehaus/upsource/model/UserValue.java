package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class UserValue
{
    private String userId;
    private int value;


    public String getUserId()
    {
        return userId;
    }


    public int getValue()
    {
        return value;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "userId", userId ).add( "value", value ).toString();
    }
}
