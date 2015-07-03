package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class PullRequestParticipant
{
    private StashUser user;
    private PullRequestRole role;
    private boolean approved;


    public StashUser getUser()
    {
        return user;
    }


    public PullRequestRole getRole()
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
