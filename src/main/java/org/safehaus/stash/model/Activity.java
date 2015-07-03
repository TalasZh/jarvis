package org.safehaus.stash.model;


import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;


public class Activity
{

    private long id;
    private long createdDate;
    private StashUser user;
    private String action;
    private String commentAction;
    private CommentAnchor commentAnchor;
    private String fromHash;
    private String previousFromHash;
    private String previousToHash;
    private String toHash;
    private ActivityChanges added;
    private ActivityChanges removed;
    private Comment comment;


    public static class Comment
    {
        private Map<String, String> properties;

        private long id;
        private long version;
        private String text;
        private StashUser author;
        private long createdDate;
        private long updatedDate;

        private Set<Comment> comments;


        public Map<String, String> getProperties()
        {
            return properties;
        }


        public long getId()
        {
            return id;
        }


        public long getVersion()
        {
            return version;
        }


        public String getText()
        {
            return text;
        }


        public StashUser getAuthor()
        {
            return author;
        }


        public Date getCreatedDate()
        {
            return new Date( createdDate );
        }


        public Date getUpdatedDate()
        {
            return new Date( updatedDate );
        }


        public Set<Comment> getComments()
        {
            return comments;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "properties", properties ).add( "id", id )
                          .add( "version", version ).add( "text", text ).add( "author", author )
                          .add( "createdDate", getCreatedDate() ).add( "updatedDate", getUpdatedDate() )
                          .add( "comments", comments ).toString();
        }
    }


    public static class CommentAnchor
    {
        private String fromHash;
        private String toHash;
        private long line;
        private String lineType;
        private String fileType;
        private String path;
        private String srcPath;
        private boolean orphaned;


        public String getFromHash()
        {
            return fromHash;
        }


        public String getToHash()
        {
            return toHash;
        }


        public long getLine()
        {
            return line;
        }


        public String getLineType()
        {
            return lineType;
        }


        public String getFileType()
        {
            return fileType;
        }


        public String getPath()
        {
            return path;
        }


        public String getSrcPath()
        {
            return srcPath;
        }


        public boolean isOrphaned()
        {
            return orphaned;
        }


        @Override
        public String toString()
        {
            return Objects.toStringHelper( this ).add( "fromHash", fromHash ).add( "toHash", toHash )
                          .add( "line", line ).add( "lineType", lineType ).add( "fileType", fileType )
                          .add( "path", path ).add( "srcPath", srcPath ).add( "orphaned", orphaned ).toString();
        }
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "createdDate", createdDate ).add( "user", user )
                      .add( "action", action ).add( "commentAction", commentAction )
                      .add( "commentAnchor", commentAnchor ).add( "fromHash", fromHash )
                      .add( "previousFromHash", previousFromHash ).add( "previousToHash", previousToHash )
                      .add( "toHash", toHash ).add( "added", added ).add( "removed", removed ).add( "comment", comment )
                      .toString();
    }
}
