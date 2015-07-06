package org.safehaus.analysis;

import java.util.Date;

/**
 * Created by kisik on 06.07.2015.
 */

public class MetricIssue {
    // Estimate values in minutes.
    private int originalEstimate;
    private int remainingEstimate;
    private int timeSpent;
    String resolutionStatus;
    String project;
    //TODO Jarvis user?
    String assignee;
    String issueType;
    String priority;
    Date created;
    Date updated;
    Date dueDate;
    Date resolutionDate;

    public int getOriginalEstimate() {
        return originalEstimate;
    }

    public void setOriginalEstimate(int originalEstimate) {
        this.originalEstimate = originalEstimate;
    }

    public int getRemainingEstimate() {
        return remainingEstimate;
    }

    public void setRemainingEstimate(int remainingEstimate) {
        this.remainingEstimate = remainingEstimate;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(String resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(Date resolutionDate) {
        this.resolutionDate = resolutionDate;
    }
}
