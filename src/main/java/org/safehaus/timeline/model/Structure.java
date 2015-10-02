package org.safehaus.timeline.model;


import java.util.Map;


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
}
