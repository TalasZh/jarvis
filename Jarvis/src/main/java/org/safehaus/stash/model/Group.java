package org.safehaus.stash.model;


import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;


public class Group
{
    private String permission;
    @SerializedName( "group" )
    private NameGroup nameGroup;


    private static class NameGroup
    {
        private String name;


        public String getName()
        {
            return name;
        }
    }


    public String getPermission()
    {
        return permission;
    }


    public String getName()
    {
        return nameGroup.getName();
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "permission", permission ).add( "name", nameGroup.getName() )
                      .toString();
    }
}
