package org.safehaus.timeline;


import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.safehaus.dao.entities.jira.JiraIssueChangelog;
import org.safehaus.model.Views;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;


/**
 * Created by talas on 9/28/15.
 */
@XmlRootElement
public class StoryTimeline
{
    @JsonView( Views.CompleteView.class )
    private List<JiraIssueChangelog> issuesEvents = Lists.newArrayList();


    public StoryTimeline()
    {
    }


    public List<JiraIssueChangelog> getIssuesEvents()
    {
        return issuesEvents;
    }


    public void setIssuesEvents( final List<JiraIssueChangelog> issuesEvents )
    {
        this.issuesEvents = issuesEvents;
    }
}
