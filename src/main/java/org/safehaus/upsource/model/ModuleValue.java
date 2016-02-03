package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class ModuleValue
{
    private String module;
    private int value;


    public String getModule()
    {
        return module;
    }


    public int getValue()
    {
        return value;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "module", module ).add( "value", value ).toString();
    }
}
