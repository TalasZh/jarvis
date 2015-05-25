package org.safehaus.model;


import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonView;


@XmlRootElement
public class JarvisIssue
{
    private static final long serialVersionUID = 3832626162173359411L;

    @JsonView( Views.JarvisIssueShort.class )
    private Long id;
    @JsonView( Views.JarvisIssueShort.class )
    private String key;
    @JsonView( Views.JarvisIssueShort.class )
    private String projectKey;
    @JsonView( Views.JarvisIssueLong.class )
    private String summary;
    //    @JsonView( Views.JarvisIssueLong.class )
    //    private Phase phase;

    @JsonView( Views.JarvisIssueShort.class )
    private String type; //Task, Session, Phase, Epic, Story etc...
    @JsonView( Views.JarvisIssueLong.class )
    private String issueDescription;
    @JsonView( Views.JarvisIssueLong.class )
    private String timeRemaining;
    @JsonView( Views.JarvisIssueLong.class )
    private String assignee;
    @JsonView( Views.JarvisIssueLong.class )
    private String reporter;
    @JsonView( Views.JarvisIssueLong.class )
    private String components;
    @JsonView( Views.JarvisIssueLong.class )
    private String labels;
    @JsonView( Views.JarvisIssueLong.class )
    private String status;
    @JsonView( Views.JarvisIssueLong.class )
    private String resolution;
    @JsonView( Views.JarvisIssueLong.class )
    private String fixVersion;
    @JsonView( Views.JarvisIssueLong.class )
    private String dateCreated;
    @JsonView( Views.JarvisIssueLong.class )
    private List<JarvisLink> links;


    public JarvisIssue()
    {
    }


    public JarvisIssue( final Long id, final String key, final String summary, final String projectKey,
                        final String type )
    {
        this.id = id;
        this.summary = summary;
        this.key = key;
        this.projectKey = projectKey;
        this.type = type;
    }


    public JarvisIssue( final Long id, final String key, final String summary, final String type,
                        final String issueDescription, final String timeRemaining, final String assignee,
                        final String reporter, final String components, final String labels, final String status,
                        final String resolution, final String fixVersion, final String dateCreated,
                        final List<JarvisLink> links, final String projectKey )
    {
        this.id = id;
        this.key = key;
        this.summary = summary;
        //        this.phase = phase;
        this.type = type;
        this.issueDescription = issueDescription;
        this.timeRemaining = timeRemaining;
        this.assignee = assignee;
        this.reporter = reporter;
        this.components = components;
        this.labels = labels;
        this.status = status;
        this.resolution = resolution;
        this.fixVersion = fixVersion;
        this.dateCreated = dateCreated;
        this.links = links;
        this.projectKey = projectKey;
    }


    public Long getId()
    {
        return id;
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
        if ( !( o instanceof JarvisIssue ) )
        {
            return false;
        }

        final JarvisIssue issue = ( JarvisIssue ) o;

        return !( id != null ? !id.equals( issue.getId() ) : issue.getId() != null );
    }


    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        return ( id != null ? id.hashCode() : 0 );
    }


    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        ToStringBuilder sb = new ToStringBuilder( this, ToStringStyle.DEFAULT_STYLE ).append( "id", this.id );
        return sb.toString();
    }
}
