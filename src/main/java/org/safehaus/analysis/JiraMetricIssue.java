package org.safehaus.analysis;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by kisik on 06.07.2015.
 */
@Entity
@Table( name = "jira_metric_issue", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "issue_id" ), @Index( name = "issue_key" )})
public class JiraMetricIssue implements Serializable {

//    @Id
    @Column(name = "issue_key")
    private String issueKey;

    @Id
    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "status")
    private String status;

    @Column(name = "issue_status")
    private String issueType;

    @Column(name = "project_key")
    private String projectKey;

    @Column(name = "reporter_name")
    private String reporterName;

    @Column(name = "assignee_name")
    private String assigneeName;

    @Column(name = "resolution")
    private String resolution;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "priority")
    private Long priority;

    @Column(name = "original_estimate_in_minutes")
    private Integer originalEstimateMinutes;

    @Column(name = "remaining_estimate_in_minutes")
    private Integer remainingEstimateMinutes;

    @Column(name = "time_spent_in_minutes")
    private Integer timeSpentMinutes;

    public String getIssueKey() {
        return issueKey;
    }

    public void setIssueKey( String key ) {
        this.issueKey = key;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId( Long id ) {
        this.issueId = id;
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


    @Override
    public String toString()
    {
        return "JiraMetricIssue{" +
                "issueKey='" + issueKey + '\'' +
                ", issueId=" + issueId +
                ", status='" + status + '\'' +
                ", issueType='" + issueType + '\'' +
                ", projectKey='" + projectKey + '\'' +
                ", reporterName='" + reporterName + '\'' +
                ", assigneeName='" + assigneeName + '\'' +
                ", resolution='" + resolution + '\'' +
                ", creationDate=" + creationDate +
                ", updateDate=" + updateDate +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", originalEstimateMinutes=" + originalEstimateMinutes +
                ", remainingEstimateMinutes=" + remainingEstimateMinutes +
                ", timeSpentMinutes=" + timeSpentMinutes +
                '}';
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof JiraMetricIssue ) )
        {
            return false;
        }

        final JiraMetricIssue that = ( JiraMetricIssue ) o;

        return issueId.equals( that.issueId );
    }


    @Override
    public int hashCode()
    {
        return issueId.hashCode();
    }
}
