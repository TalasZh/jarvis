package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class ParticipantInReview
{
    private String userId;
    private int role;
    private int state;


    public String getUserId()
    {
        return userId;
    }


    public int getRole()
    {
        return role;
    }


    public int getState()
    {
        return state;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "userId", userId ).add( "role", role ).add( "state", state )
                      .toString();
    }
}
