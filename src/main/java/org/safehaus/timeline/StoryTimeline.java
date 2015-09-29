package org.safehaus.timeline;


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
public class StoryTimeline
{
    @JsonView( Views.CompleteView.class )
    private Set<JiraMetricIssue> issues = Sets.newHashSet();


    public StoryTimeline()
    {
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
