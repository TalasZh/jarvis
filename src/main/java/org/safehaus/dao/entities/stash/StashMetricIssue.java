package org.safehaus.dao.entities.stash;


import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.stash.model.Change;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by neslihan on 08.07.2015.
 */
@XmlRootElement
@Entity
@Access( AccessType.FIELD )
@Table( name = "stash_metric_issue", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "author" ), @Index( name = "projectKey" )
} )
public class StashMetricIssue implements Serializable
{

    @EmbeddedId
    @OrderBy( "stashMetricPK.authorTs DESC" )
    private StashMetricPK stashMetricPK = new StashMetricPK();

    @OneToOne( targetEntity = Path.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Path path;

    @OneToOne( targetEntity = Path.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Path srcPath;

    @Column( name = "percent_unchanged" )
    private int percentUnchanged;

    @Enumerated( EnumType.STRING )
    @Column( name = "change_type" )
    private Change.ChangeType type;

    @Enumerated( EnumType.STRING )
    @Column( name = "node_type" )
    private Change.NodeType nodeType;

    @OneToOne( targetEntity = StashUser.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinColumn( name = "associated_user_id" )
    private StashUser author;


    @Column( name = "project_name" )
    private String projectName;

    @Column( name = "project_key" )
    private String projectKey;

    @Column( name = "commit_message" )
    private String commitMessage;

    @Column( name = "commit_uri" )
    private String uri;


    public StashMetricIssue()
    {
    }


    public StashMetricIssue( Path path, Path srcPath, int percentUnchanged, Change.ChangeType type,
                             Change.NodeType nodeType )
    {
        this.path = path;
        this.srcPath = srcPath;
        this.percentUnchanged = percentUnchanged;
        this.type = type;
        this.nodeType = nodeType;
    }


    public StashMetricPK getStashMetricPK()
    {
        return stashMetricPK;
    }


    public void setStashMetricPK( final StashMetricPK stashMetricPK )
    {
        this.stashMetricPK = stashMetricPK;
    }


    public long getAuthorTimestamp()
    {
        return stashMetricPK.getAuthorTs();
    }


    public String getId()
    {
        return stashMetricPK.getContentId();
    }


    public void setAuthorTimestamp( long authorTimestamp )
    {
        this.stashMetricPK.setAuthorTs( authorTimestamp );
    }


    public void setId( String id )
    {
        this.stashMetricPK.setContentId( id );
    }


    public Path getPath()
    {
        return path;
    }


    public Path getSrcPath()
    {
        return srcPath;
    }


    public int getPercentUnchanged()
    {
        return percentUnchanged;
    }


    public Change.ChangeType getType()
    {
        return type;
    }


    public Change.NodeType getNodeType()
    {
        return nodeType;
    }


    public String getProjectName()
    {
        return projectName;
    }


    public StashUser getAuthor()
    {
        return author;
    }


    public void setType( Change.ChangeType type )
    {
        this.type = type;
    }


    public void setPath( Path path )
    {
        this.path = path;
    }


    public void setSrcPath( Path srcPath )
    {
        this.srcPath = srcPath;
    }


    public void setPercentUnchanged( int percentUnchanged )
    {
        this.percentUnchanged = percentUnchanged;
    }


    public void setNodeType( Change.NodeType nodeType )
    {
        this.nodeType = nodeType;
    }


    public void setAuthor( StashUser author )
    {
        this.author = author;
    }


    public void setProjectName( String projectName )
    {
        this.projectName = projectName;
    }


    public void setProjectKey( String projectKey )
    {
        this.projectKey = projectKey;
    }


    public String getProjectKey()
    {
        return projectKey;
    }


    public String getCommitMessage()
    {
        return commitMessage;
    }


    public void setCommitMessage( final String commitMessage )
    {
        this.commitMessage = commitMessage;
    }


    public String getUri()
    {
        return uri;
    }


    public void setUri( final String uri )
    {
        this.uri = uri;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof StashMetricIssue ) )
        {
            return false;
        }

        final StashMetricIssue that = ( StashMetricIssue ) o;

        return !( stashMetricPK != null ? !stashMetricPK.equals( that.stashMetricPK ) : that.stashMetricPK != null );
    }


    @Override
    public int hashCode()
    {
        return stashMetricPK != null ? stashMetricPK.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "StashMetricIssue{" +
                "stashMetricPK=" + stashMetricPK +
                ", path=" + path +
                ", srcPath=" + srcPath +
                ", percentUnchanged=" + percentUnchanged +
                ", type=" + type +
                ", nodeType=" + nodeType +
                ", author=" + author +
                ", projectName='" + projectName + '\'' +
                ", projectKey='" + projectKey + '\'' +
                ", commitMessage='" + commitMessage + '\'' +
                '}';
    }
}
