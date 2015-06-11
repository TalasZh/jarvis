package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class PullRequestParticipant
{
    private User user;
    private String role;
    private boolean approved;


    public User getUser()
    {
        return user;
    }


    public String getRole()
    {
        return role;
    }


    public boolean isApproved()
    {
        return approved;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "user", user ).add( "role", role ).add( "approved", approved )
                      .toString();
    }
}
