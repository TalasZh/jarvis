package org.safehaus.analysis;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;


/**
 * Created by talas on 9/8/15.
 */
@Entity
@Table( name = "jira_issue_changelog", schema = "jarvis@cassandra-pu" )
public class JiraIssueChangelog implements Serializable
{
    @EmbeddedId
    @OrderBy( "changeKey.created DESC" )
    private ChangeCompositeKey changeKey;

    @Column( name = "issue_id" )
    private Long issueId;

    @Column( name = "issue_key" )
    private String issueKey;

    @Column( name = "author" )
    private String author;

    @Column( name = "type" )
    private String type;

    @Column( name = "field" )
    private String field;

    @Column( name = "from_string" )
    private String fromString;

    @Column( name = "to_string" )
    private String toString;

    @Column( name = "from_id" )
    private String from;

    @Column( name = "to_id" )
    private String to;


    public JiraIssueChangelog()
    {
    }


    public JiraIssueChangelog( final ChangeCompositeKey changeKey, String issueKey, Long issueId, final String author,
                               final String type,
                               final String field, final String fromString, final String toString, final String from,
                               final String to )
    {
        this.changeKey = changeKey;
        this.issueId = issueId;
        this.issueKey = issueKey;
        this.author = author;
        this.type = type;
        this.field = field;
        this.fromString = fromString;
        this.toString = toString;
        this.from = from;
        this.to = to;
    }


    public Long getIssueId()
    {
        return issueId;
    }


    public void setIssueId( final Long issueId )
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


    public ChangeCompositeKey getChangeKey()
    {
        return changeKey;
    }


    public void setChangeKey( final ChangeCompositeKey compoundKey )
    {
        this.changeKey = compoundKey;
    }


    public String getAuthor()
    {
        return author;
    }


    public void setAuthor( final String author )
    {
        this.author = author;
    }


    public String getType()
    {
        return type;
    }


    public void setType( final String type )
    {
        this.type = type;
    }


    public String getField()
    {
        return field;
    }


    public void setField( final String field )
    {
        this.field = field;
    }


    public String getFromString()
    {
        return fromString;
    }


    public void setFromString( final String fromString )
    {
        this.fromString = fromString;
    }


    public String getToString()
    {
        return toString;
    }


    public void setToString( final String toString )
    {
        this.toString = toString;
    }


    public String getFrom()
    {
        return from;
    }


    public void setFrom( final String from )
    {
        this.from = from;
    }


    public String getTo()
    {
        return to;
    }


    public void setTo( final String to )
    {
        this.to = to;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JiraIssueChangelog ) )
        {
            return false;
        }

        final JiraIssueChangelog that = ( JiraIssueChangelog ) o;

        return !( changeKey != null ? !changeKey.equals( that.changeKey ) : that.changeKey != null );
    }


    @Override
    public int hashCode()
    {
        return changeKey != null ? changeKey.hashCode() : 0;
    }
}
