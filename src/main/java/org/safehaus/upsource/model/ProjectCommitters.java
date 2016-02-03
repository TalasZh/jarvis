package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class ProjectCommitters
{
    private Set<CommitterUserInfo> users;


    public Set<CommitterUserInfo> getUsers()
    {
        return users;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "users", users ).toString();
    }
}
