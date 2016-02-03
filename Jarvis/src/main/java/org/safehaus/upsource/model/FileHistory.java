package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class FileHistory
{
    private Set<FileHistoryItem> history;
    private RevisionListGraph graph;


    public Set<FileHistoryItem> getHistory()
    {
        return history;
    }


    public RevisionListGraph getGraph()
    {
        return graph;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "history", history ).add( "graph", graph ).toString();
    }


    public static class RevisionListGraph
    {
        private int width;
        private Set<RevisionListGraphRow> rows;


        public int getWidth()
        {
            return width;
        }


        public Set<RevisionListGraphRow> getRows()
        {
            return rows;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "width", width ).add( "rows", rows ).toString();
        }
    }


    public static class RevisionListGraphRow
    {
        private Set<RevisionListGraphNode> nodes;
        private Set<RevisionListGraphEdge> edges;


        public Set<RevisionListGraphNode> getNodes()
        {
            return nodes;
        }


        public Set<RevisionListGraphEdge> getEdges()
        {
            return edges;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "nodes", nodes ).add( "edges", edges ).toString();
        }
    }


    public static class RevisionListGraphNode
    {
        private int position;
        private int color;
        private int type;


        public int getPosition()
        {
            return position;
        }


        public int getColor()
        {
            return color;
        }


        public int getType()
        {
            return type;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "position", position ).add( "color", color ).add( "type", type )
                          .toString();
        }
    }


    public static class RevisionListGraphEdge
    {
        private int position;
        private int toPosition;
        private boolean isUp;
        private boolean isSolid;
        private int color;


        public int getPosition()
        {
            return position;
        }


        public int getToPosition()
        {
            return toPosition;
        }


        public boolean isUp()
        {
            return isUp;
        }


        public boolean isSolid()
        {
            return isSolid;
        }


        public int getColor()
        {
            return color;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "position", position ).add( "toPosition", toPosition )
                          .add( "isUp", isUp ).add( "isSolid", isSolid ).add( "color", color ).toString();
        }
    }
}
