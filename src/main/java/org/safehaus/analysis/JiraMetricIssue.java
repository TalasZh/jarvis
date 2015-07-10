package org.safehaus.analysis;

import java.net.URI;
import java.util.Date;

/**
 * Created by kisik on 06.07.2015.
 */

public class JiraMetricIssue {

    private String key;
    private Long id;

    private String status;
    private String issueType;
    private String projectKey;

    private String reporterName;
    private String assigneeName;

    private String resolution;
    private Date creationDate;
    private Date updateDate;
    private Date dueDate;
    private Long priority;

    private Integer originalEstimateMinutes;
    private Integer remainingEstimateMinutes;
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
