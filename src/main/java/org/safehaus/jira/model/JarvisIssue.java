package org.safehaus.jira.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.model.Views;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import net.rcarz.jiraclient.Transition;


@XmlRootElement
@Entity
@Table( name = "jarvis_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "id" ), @Index( name = "key" )
} )
public class JarvisIssue
{
    private static final long serialVersionUID = 3832626162173359411L;

    //    @JsonView( Views.JarvisIssueLong.class )
    //    private String token="unknown";
    @JsonView( Views.JarvisIssueShort.class )
    @Id
    @Column( name = "jarvis_issue_id" )
    protected Long id;

    @JsonView( Views.JarvisIssueShort.class )
    @Column( name = "jarvis_issue_key" )
    protected String key;

    @JsonView( Views.JarvisIssueShort.class )
    @Column( name = "project_key" )
    protected String projectKey;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "summery" )
    protected String summary;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "self" )
    protected String self;
    //    @JsonView( Views.JarvisIssueLong.class )
    //    private Phase phase;

    @JsonView( Views.JarvisIssueShort.class )
    @Embedded
    protected JarvisIssueType type; //Task, Session, Phase, Epic, Story etc...

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "issue_description" )
    protected String issueDescription;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "time_remaining" )
    protected String timeRemaining;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "assignee" )
    protected String assignee;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "reporter" )
    protected String reporter;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "components" )
    protected String components;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "labels" )
    protected String labels;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "status" )
    protected String status;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "resolution" )
    protected String resolution;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "fix_version" )
    protected String fixVersion;

    @JsonView( Views.JarvisIssueLong.class )
    @Column( name = "date_created" )
    protected String dateCreated;

    @JsonView( Views.JarvisIssueLong.class )
    @OneToMany( fetch = FetchType.EAGER )
    @Column( name = "links" )
    protected List<JarvisLink> links = new ArrayList<>();

    @JsonView( Views.JarvisIssueLong.class )
    @Transient
    protected Iterable<Transition> transitions = new ArrayList<>();


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


    public JarvisIssue( final Long id, final String key, final String summary, final String self,
                        final JarvisIssueType type, final String issueDescription, final String timeRemaining,
                        final String assignee, final String reporter, final String components, final String labels,
                        final String status, final String resolution, final String fixVersion, final String dateCreated,
                        final List<JarvisLink> links, final String projectKey, Iterable<Transition> transitions )
    {
        this.id = id;
        this.key = key;
        this.summary = summary;
        this.self = self;
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
        this.transitions = transitions;
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


    public String getSelf()
    {
        return self;
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


    @JsonIgnore
    public JarvisLink getLink( String linkType, String linkDirection )
    {
        JarvisLink result = null;
        for ( JarvisLink link : links )
        {
            if ( linkType.equals( link.getLinkType() ) && linkDirection.equals( link.getLinkDirection() ) )
            {
                result = link;
            }
        }
        return result;
    }

    //
    //    public String getToken()
    //    {
    //        return token;
    //    }
    //
    //
    //    public void setToken( final String token )
    //    {
    //        this.token = token;
    //    }
    //


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
