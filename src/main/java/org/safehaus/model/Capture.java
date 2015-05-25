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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import org.apache.commons.lang.builder.ToStringBuilder;


@Entity
@Table( name = "app_capture" )
@XmlRootElement
public class Capture extends BaseObject implements Serializable
{
    private static final long serialVersionUID = 3832626162173359411L;

    private Long id;
    private Long issueId;
    private Date created = new Date();
    private Date updated = new Date();
    private String annotationText;
    private String url;
    private String ancestorId;
    private String anchorText;


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


    public Long getIssueId()
    {
        return issueId;
    }


    public void setIssueId( final Long issueId )
    {
        this.issueId = issueId;
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


    @Column( name = "updated" )
    public Date getUpdated()
    {
        return updated;
    }


    public void setUpdated( final Date updated )
    {
        this.updated = updated;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "url", url ).append( "created", created )
                                          .append( "updated", updated ).toString();
    }
}
