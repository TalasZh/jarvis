package org.safehaus.stash.model;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;


/**
 * Created by neslihan on 08.07.2015.
 */
@Entity
@Table( name = "stash_metric_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "id" ), @Index( name = "author" ), @Index( name = "authorTimestamp" )})
public class StashMetricIssue implements Serializable {

    @Id
    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    @GeneratedValue( generator = "id_gen", strategy = GenerationType.TABLE )
    @Column(name = "stash_metric_id")
    private String id;

    @OneToOne(targetEntity = Path.class)
    @AttributeOverride(name = "id", column = @Column(name = "path"))
    private Path path;

    @OneToOne(targetEntity = Path.class)
    @AttributeOverride(name = "id", column = @Column(name = "path"))
    @Column(name = "src_path")
    private Path srcPath;

    @Column(name = "percent_unchanged")
    private int percentUnchanged;

    @Enumerated( EnumType.ORDINAL )
    @Column(name = "change_type")
    private Change.ChangeType type;

    @Enumerated( EnumType.ORDINAL )
    @Column(name = "node_type")
    private Change.NodeType nodeType;

    @OneToOne(targetEntity = StashUser.class)
    @JoinColumn(name = "associated_user_id")
    private StashUser author;

    @Column(name = "author_ts")
    private long authorTimestamp;

    @Column(name = "project_name")
    //Repository->Project->name
    private String projectName;

    public StashMetricIssue(){}

    public StashMetricIssue(Path path, Path srcPath, int percentUnchanged, Change.ChangeType type, Change.NodeType nodeType)
    {
        this.path = path;
        this.srcPath = srcPath;
        this.percentUnchanged = percentUnchanged;
        this.type = type;
        this.nodeType = nodeType;
    }

    public Path getPath() {
        return path;
    }

    public Path getSrcPath() {
        return srcPath;
    }

    public int getPercentUnchanged() {
        return percentUnchanged;
    }

    public Change.ChangeType getType() {
        return type;
    }

    public Change.NodeType getNodeType() {
        return nodeType;
    }

    public String getProjectName() {
        return projectName;
    }

    public long getAuthorTimestamp() {
        return authorTimestamp;
    }

    public StashUser getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public void setType(Change.ChangeType type) {
        this.type = type;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setSrcPath(Path srcPath) {
        this.srcPath = srcPath;
    }

    public void setPercentUnchanged(int percentUnchanged) {
        this.percentUnchanged = percentUnchanged;
    }

    public void setNodeType(Change.NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setId(String id) { this.id = id; }

    public void setAuthor(StashUser author) { this.author = author; }

    public void setAuthorTimestamp(long authorTimestamp) {  this.authorTimestamp = authorTimestamp; }

    public void setProjectName(String projectName) { this.projectName = projectName; }
}
