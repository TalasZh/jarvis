package org.safehaus.stash.model;


import java.util.Map;
import java.util.Set;

import org.safehaus.dao.entities.stash.Link;
import org.safehaus.dao.entities.stash.Path;

import com.google.common.base.Objects;

public class Change
{
    private Conflict conflict;

    private String contentId;

    private String fromContentId;

    private Path path;

    private boolean executable;

    private int percentUnchanged;

    private ChangeType type;

    private NodeType nodeType;

    private Path srcPath;

    private boolean srcExecutable;

    private Link link;

    private Map<String, Set<Map<String, String>>> links;

    public enum NodeType
    {
        DIRECTORY, FILE, SUBMODULE
    }

    public enum ChangeType
    {
        ADD, COPY, DELETE, MODIFY, MOVE, UNKNOWN
    }

    public Conflict getConflict()
    {
        return conflict;
    }

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

    public ChangeType getType()
    {
        return type;
    }

    public NodeType getNodeType()
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

    public static class Conflict
    {
        private ConflictChange ourChange;

        private ConflictChange theirChange;
    }

    public static class ConflictChange
    {
        private Path path;

        private Path scrPath;

        private ChangeType type;

        public Path getPath()
        {
            return path;
        }

        public Path getScrPath()
        {
            return scrPath;
        }

        public ChangeType getType()
        {
            return type;
        }
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "conflict", conflict ).add( "contentId", contentId )
                      .add( "fromContentId", fromContentId ).add( "path", path ).add( "executable", executable )
                      .add( "percentUnchanged", percentUnchanged ).add( "type", type ).add( "nodeType", nodeType )
                      .add( "srcPath", srcPath ).add( "srcExecutable", srcExecutable ).add( "link", link )
                      .add( "links", links ).toString();
    }
}
