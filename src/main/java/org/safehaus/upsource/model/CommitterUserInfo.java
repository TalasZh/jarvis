package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class CommitterUserInfo
{
    private String committer;
    private String userId;


    public String getCommitter()
    {
        return committer;
    }


    public String getUserId()
    {
        return userId;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "committer", committer ).add( "userId", userId ).toString();
    }
}
