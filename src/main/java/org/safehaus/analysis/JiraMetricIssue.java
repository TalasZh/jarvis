package org.safehaus.analysis;

import com.impetus.kundera.index.IndexCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.net.URI;
import java.util.Date;

/**
 * Created by kisik on 06.07.2015.
 */
@Entity
@Table( name = "jira_metric_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @com.impetus.kundera.index.Index( name = "id" ), @com.impetus.kundera.index.Index( name = "key" )})
public class JiraMetricIssue implements Serializable {

    @Column
    private String key;

    @Id
    private Long id;

    @Column
    private String status;
    @Column
    private String issueType;
    @Column
    private String projectKey;

    @Column
    private String reporterName;
    @Column
    private String assigneeName;

    @Column
    private String resolution;
    @Column
    private Date creationDate;
    @Column
    private Date updateDate;
    @Column
    private Date dueDate;
    @Column
    private Long priority;

    @Column
    private Integer originalEstimateMinutes;
    @Column
    private Integer remainingEstimateMinutes;
    @Column
    private Integer timeSpentMinutes;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Integer getOriginalEstimateMinutes() {
        return originalEstimateMinutes;
    }

    public void setOriginalEstimateMinutes(Integer originalEstimateMinutes) {
        this.originalEstimateMinutes = originalEstimateMinutes;
    }

    public Integer getRemainingEstimateMinutes() {
        return remainingEstimateMinutes;
    }

    public void setRemainingEstimateMinutes(Integer remainingEstimateMinutes) {
        this.remainingEstimateMinutes = remainingEstimateMinutes;
    }

    public Integer getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public void setTimeSpentMinutes(Integer timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }
}
