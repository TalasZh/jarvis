package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class TimeValue
{
    private long time;
    private int value;


    public long getTime()
    {
        return time;
    }


    public int getValue()
    {
        return value;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "time", time ).add( "value", value ).toString();
    }
}
