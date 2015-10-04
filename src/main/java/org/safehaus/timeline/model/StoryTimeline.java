package org.safehaus.timeline.model;


import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;


/**
 * Created by talas on 9/28/15.
 */
@XmlRootElement
public class StoryTimeline extends JiraMetricIssue
{
    @JsonView( Views.CompleteView.class )
    private Set<JiraMetricIssue> issues = Sets.newHashSet();


    public StoryTimeline()
    {
        super();
    }


    public StoryTimeline( JiraMetricIssue jiraMetricIssue )
    {
        super();
        setAssigneeName( jiraMetricIssue.getAssigneeName() );
        setChangelogList( jiraMetricIssue.getChangelogList() );
        setCreationDate( jiraMetricIssue.getCreationDate() );
        setDescription( jiraMetricIssue.getDescription() );
        setDueDate( jiraMetricIssue.getDueDate() );
        setIssueId( jiraMetricIssue.getIssueId() );
        setIssueKey( jiraMetricIssue.getIssueKey() );
        setIssueLinks( jiraMetricIssue.getIssueLinks() );
        setOriginalEstimateMinutes( jiraMetricIssue.getOriginalEstimateMinutes() );
        setPriority( jiraMetricIssue.getPriority() );
        setProjectKey( jiraMetricIssue.getProjectKey() );
        setRemainingEstimateMinutes( jiraMetricIssue.getRemainingEstimateMinutes() );
        setReporterName( jiraMetricIssue.getReporterName() );
        setResolution( jiraMetricIssue.getResolution() );
        setStatus( jiraMetricIssue.getStatus() );
        setSummary( jiraMetricIssue.getSummary() );
        setTimeSpentMinutes( jiraMetricIssue.getTimeSpentMinutes() );
        setType( jiraMetricIssue.getType() );
        setUpdateDate( jiraMetricIssue.getUpdateDate() );
        setComponents( jiraMetricIssue.getComponents() );
        setIssueWorkLogs( jiraMetricIssue.getIssueWorkLogs() );
        setLabels( jiraMetricIssue.getLabels() );
    }


    public Set<JiraMetricIssue> getIssues()
    {
        return issues;
    }


    public void setIssues( final Set<JiraMetricIssue> issues )
    {
        this.issues = issues;
    }
}
