package org.safehaus.timeline.model;


import java.util.Map;
import java.util.Set;


/**
 * Created by talas on 9/30/15.
 */
public interface Structure
{
    public ProgressStatus getOpenStatus();


    public void setOpenStatus( final ProgressStatus openStatus );


    public ProgressStatus getInProgressStatus();


    public void setInProgressStatus( final ProgressStatus inProgressStatus );


    public ProgressStatus getDoneStatus();


    public void setDoneStatus( final ProgressStatus doneStatus );


    public Map<String, Long> getTotalIssuesSolved();


    public void setTotalIssuesSolved( final Map<String, Long> totalIssuesSolved );

    public Set<String> getUsers();


    public void setUsers( final Set<String> usernames );


    public IssueProgress getStoryPoints();


    public void setStoryPoints( final IssueProgress storyPoints );


    public IssueProgress getStoryProgress();


    public void setStoryProgress( final IssueProgress storyProgress );
}
