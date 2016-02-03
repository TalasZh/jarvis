package org.safehaus.dao.entities.stash;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;

@Entity
@Table( name = "stash_commit", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "id" ), @Index( name = "author" ), @Index( name = "displayId" )
} )
public class Commit
{
    @Id
    @Column(name = "commit_id")
    private String id;

    @Column( name = "stash_commit_displayid" )
    private String displayId;

    @OneToOne( targetEntity = StashUser.class )
    @JoinColumn( name = "id" )
    private StashUser author;

    @Column( name = "stash_commit_author_ts" )
    private long authorTimestamp;

    @Column( name = "stash_commit_msg" )
    private String message;

    @ElementCollection( targetClass = MinimalCommit.class, fetch = FetchType.EAGER )
    @Column( name = "stash_commit_parents" )
    private Set<MinimalCommit> parents = new HashSet<>();


    public String getId()
    {
        return id;
    }


    public String getDisplayId()
    {
        return displayId;
    }


    public StashUser getAuthor()
    {
        return author;
    }


    public long getAuthorTimestamp()
    {
        return authorTimestamp;
    }


    public String getMessage()
    {
        return message;
    }


    public Set<MinimalCommit> getParents()
    {
        return parents;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "id", id ).add( "displayId", displayId ).add( "author", author )
                      .add( "authorTimestamp", authorTimestamp ).add( "message", message ).add( "parents", parents )
                      .toString();
    }
}
