package org.safehaus.stash.model;

import com.impetus.kundera.index.IndexCollection;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by neslihan on 08.07.2015.
 */
@Entity
@Table( name = "stash_metric_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @com.impetus.kundera.index.Index( name = "id" ), @com.impetus.kundera.index.Index( name = "author" ), @com.impetus.kundera.index.Index( name = "authorTimestamp" )})
public class StashMetricIssue implements Serializable {

    @Id
    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    @GeneratedValue( generator = "id_gen", strategy = GenerationType.TABLE )
    private long id;

    @OneToOne(targetEntity = Path.class)
    @AttributeOverride(name = "id", column = @Column(name = "path"))
    private Path path;

    @OneToOne(targetEntity = Path.class)
    @AttributeOverride(name = "id", column = @Column(name = "path"))
    private Path srcPath;

    @Column
    private int percentUnchanged;

    @Enumerated( EnumType.ORDINAL )
    @Column(name = "type")
    private Change.ChangeType type;

    @Enumerated( EnumType.ORDINAL )
    @Column(name = "type")
    private Change.NodeType nodeType;

    @OneToOne(targetEntity = StashUser.class)
    @JoinColumn(name = "id")
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

    public long getId() {
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

    public void setId(long id) { this.id = id; }

    public void setAuthor(StashUser author) { this.author = author; }

    public void setAuthorTimestamp(long authorTimestamp) {  this.authorTimestamp = authorTimestamp; }

    public void setProjectName(String projectName) { this.projectName = projectName; }
}
