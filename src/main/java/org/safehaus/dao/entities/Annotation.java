package org.safehaus.dao.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by talas on 10/4/15.
 */
@Entity
@Table( name = "research_annotation", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "author" ), @Index( name = "created" ), @Index( name = "issueKey" ), @Index( name = "issueId" )
} )
public class Annotation implements Serializable
{
    @Id
    @Column( name = "annotation_id" )
    private Long id;

    @Column( name = "author" )
    private String author;

    @Column( name = "issue_id" )
    private String issueId;

    @Column( name = "issue_key" )
    private String issueKey;

    @Column( name = "annotator_schema_version" )
    private String annotatorSchemaVersion;

    @Column( name = "created" )
    @OrderBy( "created DESC " )
    private Long created;

    @Column( name = "quote" )
    private String quote;

    @Column( name = "ranges" )
    private String ranges;

    @Column( name = "text" )
    private String text;

    @Column( name = "uri" )
    private String uri;

    @Column( name = "offline_id" )
    private String offlineId;


    public Annotation()
    {
    }


    public Annotation( final Long id, final String author, final String issueId, final String issueKey,
                       final String annotatorSchemaVersion, final Long created, final String quote, final String ranges,
                       final String text, final String uri, final String offlineId )
    {
        this.id = id;
        this.author = author;
        this.issueId = issueId;
        this.issueKey = issueKey;
        this.annotatorSchemaVersion = annotatorSchemaVersion;
        this.created = created;
        this.quote = quote;
        this.ranges = ranges;
        this.text = text;
        this.uri = uri;
        this.offlineId = offlineId;
    }


    public Long getId()
    {
        return id;
    }


    public void setId( final Long id )
    {
        this.id = id;
    }


    public String getAuthor()
    {
        return author;
    }


    public void setAuthor( final String author )
    {
        this.author = author;
    }


    public String getIssueId()
    {
        return issueId;
    }


    public void setIssueId( final String issueId )
    {
        this.issueId = issueId;
    }


    public String getIssueKey()
    {
        return issueKey;
    }


    public void setIssueKey( final String issueKey )
    {
        this.issueKey = issueKey;
    }


    public String getAnnotatorSchemaVersion()
    {
        return annotatorSchemaVersion;
    }


    public void setAnnotatorSchemaVersion( final String annotatorSchemaVersion )
    {
        this.annotatorSchemaVersion = annotatorSchemaVersion;
    }


    public Long getCreated()
    {
        return created;
    }


    public void setCreated( final Long created )
    {
        this.created = created;
    }


    public String getQuote()
    {
        return quote;
    }


    public void setQuote( final String quote )
    {
        this.quote = quote;
    }


    public String getRanges()
    {
        return ranges;
    }


    public void setRanges( final String ranges )
    {
        this.ranges = ranges;
    }


    public String getText()
    {
        return text;
    }


    public void setText( final String text )
    {
        this.text = text;
    }


    public String getUri()
    {
        return uri;
    }


    public void setUri( final String uri )
    {
        this.uri = uri;
    }


    public String getOfflineId()
    {
        return offlineId;
    }


    public void setOfflineId( final String offlineId )
    {
        this.offlineId = offlineId;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof Annotation ) )
        {
            return false;
        }

        final Annotation that = ( Annotation ) o;

        return !( id != null ? !id.equals( that.id ) : that.id != null );
    }


    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "Annotation{" +
                "author='" + author + '\'' +
                ", id=" + id +
                ", created=" + created +
                '}';
    }
}
