package org.safehaus.stash.model;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;


public class Change
{
    private String contentId;
    private String fromContentId;
    private Path path;
    private boolean executable;
    private int percentUnchanged;
    private String type;
    private String nodeType;
    private Path srcPath;
    private boolean srcExecutable;
    private Link link;
    private Map<String, Set<Map<String, String>>> links;


    public String getContentId()
    {
        return contentId;
    }


    public String getFromContentId()
    {
        return fromContentId;
    }


    public Path getPath()
    {
        return path;
    }


    public boolean isExecutable()
    {
        return executable;
    }


    public int getPercentUnchanged()
    {
        return percentUnchanged;
    }


    public String getType()
    {
        return type;
    }


    public String getNodeType()
    {
        return nodeType;
    }


    public Path getSrcPath()
    {
        return srcPath;
    }


    public boolean isSrcExecutable()
    {
        return srcExecutable;
    }


    public Link getLink()
    {
        return link;
    }


    public Map<String, Set<Map<String, String>>> getLinks()
    {
        return links;
    }


    public static class Path
    {
        private List<String> components;

        private String parent;
        private String name;
        private String extension;
        private String toString;


        public List<String> getComponents()
        {
            return components;
        }


        public String getParent()
        {
            return parent;
        }


        public String getName()
        {
            return name;
        }


        public String getExtension()
        {
            return extension;
        }


        public String getToString()
        {
            return toString;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "components", components ).add( "parent", parent )
                          .add( "name", name ).add( "extension", extension ).add( "toString", toString ).toString();
        }
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "contentId", contentId ).add( "fromContentId", fromContentId )
                      .add( "path", path ).add( "executable", executable ).add( "percentUnchanged", percentUnchanged )
                      .add( "type", type ).add( "nodeType", nodeType ).add( "srcPath", srcPath )
                      .add( "srcExecutable", srcExecutable ).add( "link", link ).add( "links", links ).toString();
    }
}
