package org.safehaus.model;


import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonView;


@XmlRootElement
public class JarvisIssue
{
    private static final long serialVersionUID = 3832626162173359411L;

    @JsonView( Views.JarvisIssueLong.class )
    private String token="unknown";
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
    private JarvisIssueType type; //Task, Session, Phase, Epic, Story etc...
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
                        final JarvisIssueType type )
    {
        this.id = id;
        this.summary = summary;
        this.key = key;
        this.projectKey = projectKey;
        this.type = type;
    }


    public JarvisIssue( final Long id, final String key, final String summary, final JarvisIssueType type,
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


    public void setId( final Long id )
    {
        this.id = id;
    }


    public String getKey()
    {
        return key;
    }


    public void setKey( final String key )
    {
        this.key = key;
    }


    public String getProjectKey()
    {
        return projectKey;
    }


    public void setProjectKey( final String projectKey )
    {
        this.projectKey = projectKey;
    }


    public String getSummary()
    {
        return summary;
    }


    public void setSummary( final String summary )
    {
        this.summary = summary;
    }


    public JarvisIssueType getType()
    {
        return type;
    }


    public void setType( final JarvisIssueType type )
    {
        this.type = type;
    }


    public String getIssueDescription()
    {
        return issueDescription;
    }


    public void setIssueDescription( final String issueDescription )
    {
        this.issueDescription = issueDescription;
    }


    public String getTimeRemaining()
    {
        return timeRemaining;
    }


    public void setTimeRemaining( final String timeRemaining )
    {
        this.timeRemaining = timeRemaining;
    }


    public String getAssignee()
    {
        return assignee;
    }


    public void setAssignee( final String assignee )
    {
        this.assignee = assignee;
    }


    public String getReporter()
    {
        return reporter;
    }


    public void setReporter( final String reporter )
    {
        this.reporter = reporter;
    }


    public String getComponents()
    {
        return components;
    }


    public void setComponents( final String components )
    {
        this.components = components;
    }


    public String getLabels()
    {
        return labels;
    }


    public void setLabels( final String labels )
    {
        this.labels = labels;
    }


    public String getStatus()
    {
        return status;
    }


    public void setStatus( final String status )
    {
        this.status = status;
    }


    public String getResolution()
    {
        return resolution;
    }


    public void setResolution( final String resolution )
    {
        this.resolution = resolution;
    }


    public String getFixVersion()
    {
        return fixVersion;
    }


    public void setFixVersion( final String fixVersion )
    {
        this.fixVersion = fixVersion;
    }


    public String getDateCreated()
    {
        return dateCreated;
    }


    public void setDateCreated( final String dateCreated )
    {
        this.dateCreated = dateCreated;
    }


    public List<JarvisLink> getLinks()
    {
        return links;
    }


    public void setLinks( final List<JarvisLink> links )
    {
        this.links = links;
    }


    public String getToken()
    {
        return token;
    }


    public void setToken( final String token )
    {
        this.token = token;
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
    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "id", id ).append( "key", key ).append( "projectKey", projectKey )
                                          .append( "summary", summary ).append( "type", type )
                                          .append( "issueDescription", issueDescription )
                                          .append( "timeRemaining", timeRemaining ).append( "assignee", assignee )
                                          .append( "reporter", reporter ).append( "components", components )
                                          .append( "labels", labels ).append( "status", status )
                                          .append( "resolution", resolution ).append( "fixVersion", fixVersion )
                                          .append( "dateCreated", dateCreated ).append( "links", links ).toString();
    }
}
