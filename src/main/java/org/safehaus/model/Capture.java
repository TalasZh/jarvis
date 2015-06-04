package org.safehaus.model;


import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table( name = "app_capture" )
@XmlRootElement
public class Capture extends BaseObject implements Serializable
{
    private static final long serialVersionUID = 3832626162173359411L;

    private Long id;
    private Session session;
    private Date created = new Date();
    private String annotationText;
    private String url;
    private String ancestorId;
    private String anchorText;
    private String comment;


    /**
     * Default constructor - creates a new instance with no values set.
     */
    public Capture()
    {
    }


    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    public Long getId()
    {
        return id;
    }


    public void setId( final Long id )
    {
        this.id = id;
    }


    @ManyToOne()
    @JoinColumn( name = "session_id" )
    @JsonIgnore
    @XmlTransient
    public Session getSession()
    {
        return session;
    }


    @JsonProperty
    @XmlElement
    @Transient
    public String getJiraKey()
    {
        if ( session != null )
        {
            return session.getIssueKey();
        }
        else
        {
            return null;
        }
    }


    public void setSession( final Session session )
    {
        this.session = session;
    }


    public String getAnnotationText()
    {
        return annotationText;
    }


    public void setAnnotationText( final String annotationText )
    {
        this.annotationText = annotationText;
    }


    public String getUrl()
    {
        return url;
    }


    public void setUrl( final String url )
    {
        this.url = url;
    }


    public String getAncestorId()
    {
        return ancestorId;
    }


    public void setAncestorId( final String ancestorId )
    {
        this.ancestorId = ancestorId;
    }


    public String getAnchorText()
    {
        return anchorText;
    }


    public void setAnchorText( final String anchorText )
    {
        this.anchorText = anchorText;
    }


    public String getComment()
    {
        return comment;
    }


    public void setComment( final String comment )
    {
        this.comment = comment;
    }


    /**
     * {@inheritDoc}
     */
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof Capture ) )
        {
            return false;
        }

        final Capture capture = ( Capture ) o;

        return !( capture != null ? !capture.equals( capture.getId() ) : capture.getId() != null );
    }


    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return ( id != null ? id.hashCode() : 0 );
    }


    @Column( name = "created" )
    public Date getCreated()
    {
        return created;
    }


    public void setCreated( final Date created )
    {
        this.created = created;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "url", url ).append( "created", created )
                                          .append( "sessionId", session != null ? session.getId() : null ).toString();
    }
}
