package org.safehaus.model;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table( name = "app_capture" )
@XmlRootElement
@JsonIgnoreProperties( ignoreUnknown = true )
public class Capture extends BaseObject implements Serializable {
    private static final long serialVersionUID = 3832626162173359411L;

    private Long id;
    private Session session;
    private Date created = new Date();

    private String researchSession;
    private String uri;
    private String ranges;
    private String quote;
    private String text;

    @Column( name = "offline_id" )
    private String offlineId;
    private String annotator_schema_version;


    /**
     * Default constructor - creates a new instance with no values set.
     */
    public Capture() {
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
    public Session getSession() {
        return session;
    }


    @JsonProperty
    @XmlElement
    @Transient
    public String getJiraKey() {
        if ( session != null ) {
            return session.getIssueKey();
        } else {
            return null;
        }
    }


    public void setSession( final Session session ) {
        this.session = session;
    }


    public String getResearchSession() {
        return researchSession;
    }


    public void setResearchSession( final String researchSession ) {
        this.researchSession = researchSession;
    }


    public String getUri() {
        return uri;
    }


    public void setUri( final String uri ) {
        this.uri = uri;
    }


    public String getRanges() {
        return ranges;
    }


    public void setRanges( final String ranges ) {
        this.ranges = ranges;
    }


    public String getQuote() {
        return quote;
    }


    public void setQuote( final String quote ) {
        this.quote = quote;
    }


    public String getText() {
        return text;
    }


    public void setText( final String text ) {
        this.text = text;
    }


    public String getOfflineId()
    {
        return offlineId;
    }


    public void setOfflineId( final String localId )
    {
        this.offlineId = localId;
    }


    public String getAnnotator_schema_version() {
        return annotator_schema_version;
    }


    public void setAnnotator_schema_version( final String annotator_schema_version ) {
        this.annotator_schema_version = annotator_schema_version;
    }


    /**
     * {@inheritDoc}
     */
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof Capture ) ) {
            return false;
        }

        final Capture capture = ( Capture ) o;

        return !( capture != null ? !capture.equals( capture.getId() ) : capture.getId() != null );
    }


    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return ( id != null ? id.hashCode() : 0 );
    }


    @Column( name = "created" )
    public Date getCreated() {
        return created;
    }


    public void setCreated( final Date created ) {
        this.created = created;
    }


    @Override
    public String toString() {
        return new ToStringBuilder( this ).append( "id", id )
                                          .append( "url", uri )
                                          .append( "created", created )
                                          .append( "sessionId", session != null ? session.getId() : null ).toString();
    }
}
