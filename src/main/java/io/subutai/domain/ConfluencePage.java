package io.subutai.domain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;


public class ConfluencePage
{
    //required Page|Blog
    @Expose
    private String type;
    @Expose
    private String title;
    @Expose
    private Map<String, String> space = new HashMap<>();
    @Expose
    private Body body;
    @Expose
    private List<Map> ancestors = new ArrayList<>();


    public Body getBody()
    {
        return body;
    }


    public void setBody( final Body body )
    {
        this.body = body;
    }


    public List<Map> getAncestors()
    {
        return ancestors;
    }


    public void setAncestors( final List<Map> ancestors )
    {
        this.ancestors = ancestors;
    }


    public String getType()
    {
        return type;
    }


    public void setType( final String type )
    {
        this.type = type;
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle( final String title )
    {
        this.title = title;
    }


    public Map<String, String> getSpace()
    {
        return space;
    }


    public void setSpace( final Map<String, String> space )
    {
        this.space = space;
    }


    public static class Body
    {
        @Expose
        private Map<String, String> storage = new HashMap<>();


        public Map<String, String> getStorage()
        {
            return storage;
        }


        public void setStorage( final Map<String, String> storage )
        {
            this.storage = storage;
        }
    }
}
