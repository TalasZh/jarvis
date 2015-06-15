package org.safehaus.stash.model;


import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;


public class Repository
{
    private String slug;
    private long id;
    private String name;
    private String scmId;
    private State state;
    private String statusMessage;
    private boolean forkable;
    private Project project;
    @SerializedName( "public" )
    private boolean isPublic;
    private Link link;
    private String cloneUrl;
    private Map<String, Set<Map<String, String>>> links;


    public enum State
    {

        AVAILABLE, INITIALISATION_FAILED, INITIALISING
    }


    public String getSlug()
    {
        return slug;
    }


    public long getId()
    {
        return id;
    }


    public String getName()
    {
        return name;
    }


    public String getScmId()
    {
        return scmId;
    }


    public State getState()
    {
        return state;
    }


    public String getStatusMessage()
    {
        return statusMessage;
    }


    public boolean isForkable()
    {
        return forkable;
    }


    public Project getProject()
    {
        return project;
    }


    public boolean isPublic()
    {
        return isPublic;
    }


    public Link getLink()
    {
        return link;
    }


    public String getCloneUrl()
    {
        return cloneUrl;
    }


    public Map<String, Set<Map<String, String>>> getLinks()
    {
        return links;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "slug", slug ).add( "id", id ).add( "name", name )
                      .add( "scmId", scmId ).add( "state", state ).add( "statusMessage", statusMessage )
                      .add( "forkable", forkable ).add( "project", project ).add( "isPublic", isPublic )
                      .add( "link", link ).add( "cloneUrl", cloneUrl ).add( "links", links ).toString();
    }
}
