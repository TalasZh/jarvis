package org.safehaus.timeline.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 10/3/15.
 */
@Embeddable
public class StoryPoints
{
    @Column( name = "in_progress" )
    private long inProgress;

    @Column( name = "done" )
    private long done;

    @Column( name = "open" )
    private long open;


    public StoryPoints( final long inProgress, final long done, final long open )
    {
        this.inProgress = inProgress;
        this.done = done;
        this.open = open;
    }


    public StoryPoints()
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
        return "StoryPoints{" +
                "inProgress=" + inProgress +
                ", done=" + done +
                ", open=" + open +
                '}';
    }
}
