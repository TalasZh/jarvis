package org.safehaus.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * Created by tzhamakeev on 5/27/15.
 */
@Entity
@Table( name = "app_session" )
@XmlRootElement
public class Session extends BaseObject
{
    @JsonView( Views.JarvisSessionShort.class )
    private Long id;

    @JsonView( Views.JarvisSessionShort.class )
    private String username;

    @JsonView( Views.JarvisSessionShort.class )
    private Long issueId;

    @JsonView( Views.JarvisSessionShort.class )
    private String issueKey;

    @JsonView( Views.JarvisSessionShort.class )
    private SessionStatus status = SessionStatus.OPEN;

    @JsonView( Views.JarvisSessionShort.class )
    private Date created = new Date();

    @JsonView( Views.JarvisSessionShort.class )
    private Long parentId;

    @JsonView( Views.JarvisSessionLong.class )
    private Set<Capture> captures = new HashSet<>();


    public Session()
    {
    }


    @Id
    //    @GeneratedValue( strategy = GenerationType.AUTO )
    public Long getId()
    {
        return id;
    }


    public void setId( final Long id )
    {
        this.id = id;
    }


    public String getUsername()
    {
        return username;
    }


    public void setUsername( final String username )
    {
        this.username = username;
    }


    @Temporal( TemporalType.TIMESTAMP )
    public Date getCreated()
    {
        return created;
    }


    public void setCreated( final Date created )
    {
        this.created = created;
    }


    @Enumerated( EnumType.STRING )
    public SessionStatus getStatus()
    {
        return status;
    }


    public void setStatus( final SessionStatus status )
    {
        this.status = status;
    }


    @OneToMany( mappedBy = "session", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @Fetch( FetchMode.SUBSELECT )
    public Set<Capture> getCaptures()
    {
        return captures;
    }


    public void setCaptures( final Set<Capture> captures )
    {
        this.captures = captures;
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


    public Long getParentId()
    {
        return parentId;
    }


    public void setParentId( final Long parentId )
    {
        this.parentId = parentId;
    }


    public void addCapture( Capture capture )
    {
        if ( capture == null )
        {
            throw new IllegalArgumentException( "Capture could not be null." );
        }

        capture.setSession( this );
        captures.add( capture );
    }


    public Capture updateCapture( final Long captureId, final Capture capture )
    {
        Capture result = null;

        for ( Iterator<Capture> captureIterator = captures.iterator(); captureIterator.hasNext(); )
        {
            Capture c = captureIterator.next();
            if ( captureId.equals( c.getId() ) )
            {
                result = c;
                break;
            }
        }
        if ( result == null )
        {
            throw new IllegalArgumentException( "Capture not found." );
        }

        result.setAncestorId( capture.getAncestorId() );
        result.setAnnotationText( capture.getAnnotationText() );
        result.setComment( capture.getComment() );
        result.setAnchorText( capture.getAnchorText() );
        result.setUrl( capture.getUrl() );
        return result;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof Session ) )
        {
            return false;
        }

        final Session session = ( Session ) o;

        if ( id != null ? !id.equals( session.id ) : session.id != null )
        {
            return false;
        }
        if ( username != null ? !username.equals( session.username ) : session.username != null )
        {
            return false;
        }
        if ( status != session.status )
        {
            return false;
        }
        return !( captures != null ? !captures.equals( session.captures ) : session.captures != null );
    }


    @Override
    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + ( username != null ? username.hashCode() : 0 );
        result = 31 * result + ( status != null ? status.hashCode() : 0 );
        result = 31 * result + ( captures != null ? captures.hashCode() : 0 );
        return result;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "username", username ).append( "status", status )
                                          .append( "captures", captures ).toString();
    }
}
