package org.safehaus.timeline.model;


import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.stash.StashMetricIssue;
import org.safehaus.model.Capture;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;


/**
 * Created by talas on 9/28/15.
 */
@XmlRootElement
public class StoryTimeline extends JiraMetricIssue
{
    @JsonView( Views.CompleteView.class )
    private Set<StoryTimeline> issues = Sets.newHashSet();

    private Set<Capture> annotations = Sets.newHashSet();

    private Set<StashMetricIssue> commits = Sets.newHashSet();


    public StoryTimeline()
    {
        super();
    }


    public StoryTimeline( JiraMetricIssue jiraMetricIssue )
    {
        super();
        Preconditions.checkNotNull( jiraMetricIssue, "JiraMetricIssue is invalid" );
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
        setRemoteLinks( jiraMetricIssue.getRemoteLinks() );
        setGitCommits( jiraMetricIssue.getGitCommits() );

        //        jiraMetricIssue.getAssigneeName();
        //        jiraMetricIssue.getChangelogList();
        //        jiraMetricIssue.getComponents();
        //        jiraMetricIssue.getCreationDate();
        //        jiraMetricIssue.getDescription();
        //        jiraMetricIssue.getDueDate();
        //        jiraMetricIssue.getIssueId();
        //        jiraMetricIssue.getIssueKey();
        //        jiraMetricIssue.getIssueLinks();
        //        jiraMetricIssue.getIssueWorkLogs();
        //        jiraMetricIssue.getLabels();
        //        jiraMetricIssue.getOriginalEstimateMinutes();
        //        jiraMetricIssue.getPriority();
        //        jiraMetricIssue.getProjectKey();
        //        jiraMetricIssue.getRemainingEstimateMinutes();
        //        jiraMetricIssue.getReporterName();
        //        jiraMetricIssue.getRemoteLinks();
        //        jiraMetricIssue.getResolution();
        //        jiraMetricIssue.getStatus();
        //        jiraMetricIssue.getSummary();
        //        jiraMetricIssue.getTimeSpentMinutes();
        //        jiraMetricIssue.getType();
        //        jiraMetricIssue.getUpdateDate();
    }


    @Override
    @JsonIgnore
    public Set<String> getGitCommits()
    {
        return super.getGitCommits();
    }


    public Set<StashMetricIssue> getCommits()
    {
        return commits;
    }


    public Set<StoryTimeline> getIssues()
    {
        return issues;
    }


    public void setIssues( final Set<StoryTimeline> issues )
    {
        this.issues = issues;
    }


    public Set<Capture> getAnnotations()
    {
        return annotations;
    }


    public void setAnnotations( final Set<Capture> annotations )
    {
        this.annotations = annotations;
    }
}
