package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class PullRequestRef
{
    private String id;
    private String displayId;
    private String latestChangeset;
    private String latestCommit;
    private Repository repository;


    public String getId()
    {
        return id;
    }


    public String getDisplayId()
    {
        return displayId;
    }


    public String getLatestCommit()
    {
        return latestCommit;
    }


    public String getLatestChangeset()
    {
        return latestChangeset;
    }


    public Repository getRepository()
    {
        return repository;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "displayId", displayId )
                      .add( "latestChangeset", latestChangeset ).add( "latestCommit", latestCommit )
                      .add( "repository", repository ).toString();
    }
}
