package org.safehaus.timeline.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 10/3/15.
 */


/**
 * This class used for various purposes like continue with counter for different issue types, according to issue status
 * state
 */
@Embeddable
public class IssueProgress
{
    @Column( name = "in_progress" )
    private long inProgress = 0L;

    @Column( name = "done" )
    private long done = 0L;

    @Column( name = "open" )
    private long open = 0L;


    public IssueProgress( final long inProgress, final long done, final long open )
    {
        this.inProgress = inProgress;
        this.done = done;
        this.open = open;
    }


    public IssueProgress()
    {
    }


    public long getInProgress()
    {
        return inProgress;
    }


    public void setInProgress( final long inProgress )
    {
        this.inProgress = inProgress;
    }


    public long getDone()
    {
        return done;
    }


    public void setDone( final long done )
    {
        this.done = done;
    }


    public long getOpen()
    {
        return open;
    }


    public void setOpen( final long open )
    {
        this.open = open;
    }


    @Override
    public String toString()
    {
        return "IssueProgress{" +
                "inProgress=" + inProgress +
                ", done=" + done +
                ", open=" + open +
                '}';
    }
}
