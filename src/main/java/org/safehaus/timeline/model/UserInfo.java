package org.safehaus.timeline.model;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.safehaus.dao.entities.jira.JiraIssueChangelog;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


/**
 * Created by talas on 10/4/15.
 */

public class UserInfo implements Serializable
{
    private String userId;

    private String username;

    private String email;

    private String displayName;

    private Map<String, Long> totalIssuesSolved = Maps.newHashMap();

    private ProgressStatus openStatus = new ProgressStatus();

    private ProgressStatus inProgressStatus = new ProgressStatus();

    private ProgressStatus doneStatus = new ProgressStatus();

    private Set<StructuredProject> projects = Sets.newHashSet();

    private IssueProgress storyPoints = new IssueProgress();

    private List<JiraIssueChangelog> recentActivity = Lists.newArrayList();


    public UserInfo()
    {
    }


    public List<JiraIssueChangelog> getRecentActivity()
    {
        return recentActivity;
    }


    public void setRecentActivity( final List<JiraIssueChangelog> recentActivity )
    {
        this.recentActivity = recentActivity;
    }


    public IssueProgress getStoryPoints()
    {
        return storyPoints;
    }


    public void setStoryPoints( final IssueProgress storyPoints )
    {
        this.storyPoints = storyPoints;
    }


    public Set<StructuredProject> getProjects()
    {
        return projects;
    }


    public void setProjects( final Set<StructuredProject> projects )
    {
        this.projects = projects;
    }


    public String getUserId()
    {
        return userId;
    }


    public void setUserId( final String userId )
    {
        this.userId = userId;
    }


    public String getUsername()
    {
        return username;
    }


    public void setUsername( final String username )
    {
        this.username = username;
    }


    public String getEmail()
    {
        return email;
    }


    public void setEmail( final String email )
    {
        this.email = email;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public void setDisplayName( final String displayName )
    {
        this.displayName = displayName;
    }


    public Map<String, Long> getTotalIssuesSolved()
    {
        return totalIssuesSolved;
    }


    public void setTotalIssuesSolved( final Map<String, Long> totalIssuesSolved )
    {
        this.totalIssuesSolved = totalIssuesSolved;
    }


    public ProgressStatus getOpenStatus()
    {
        return openStatus;
    }


    public void setOpenStatus( final ProgressStatus openStatus )
    {
        this.openStatus = openStatus;
    }


    public ProgressStatus getInProgressStatus()
    {
        return inProgressStatus;
    }


    public void setInProgressStatus( final ProgressStatus inProgressStatus )
    {
        this.inProgressStatus = inProgressStatus;
    }


    public ProgressStatus getDoneStatus()
    {
        return doneStatus;
    }


    public void setDoneStatus( final ProgressStatus doneStatus )
    {
        this.doneStatus = doneStatus;
    }
}
