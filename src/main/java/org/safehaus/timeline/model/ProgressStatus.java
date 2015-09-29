package org.safehaus.timeline.model;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Created by talas on 9/30/15.
 */
@Embeddable
public class ProgressStatus implements Serializable
{
    @Column( name = "original_estimate" )
    private Long originalEstimate;

    @Column( name = "remaining_estimate" )
    private Long remainingRestimate;

    @Column( name = "time_spent" )
    private Long timeSpent;


    public ProgressStatus()
    {
    }


    public ProgressStatus( final Long originalEstimate, final Long remainingRestimate, final Long timeSpent )
    {
        this.originalEstimate = originalEstimate;
        this.remainingRestimate = remainingRestimate;
        this.timeSpent = timeSpent;
    }


    public Long getOriginalEstimate()
    {
        return originalEstimate;
    }


    public void setOriginalEstimate( final Long originalEstimate )
    {
        this.originalEstimate = originalEstimate;
    }


    public Long getRemainingRestimate()
    {
        return remainingRestimate;
    }


    public void setRemainingRestimate( final Long remainingRestimate )
    {
        this.remainingRestimate = remainingRestimate;
    }


    public Long getTimeSpent()
    {
        return timeSpent;
    }


    public void setTimeSpent( final Long timeSpent )
    {
        this.timeSpent = timeSpent;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof ProgressStatus ) )
        {
            return false;
        }

        final ProgressStatus that = ( ProgressStatus ) o;

        if ( originalEstimate != null ? !originalEstimate.equals( that.originalEstimate ) :
             that.originalEstimate != null )
        {
            return false;
        }
        if ( remainingRestimate != null ? !remainingRestimate.equals( that.remainingRestimate ) :
             that.remainingRestimate != null )
        {
            return false;
        }
        return !( timeSpent != null ? !timeSpent.equals( that.timeSpent ) : that.timeSpent != null );
    }


    @Override
    public int hashCode()
    {
        int result = originalEstimate != null ? originalEstimate.hashCode() : 0;
        result = 31 * result + ( remainingRestimate != null ? remainingRestimate.hashCode() : 0 );
        result = 31 * result + ( timeSpent != null ? timeSpent.hashCode() : 0 );
        return result;
    }


    @Override
    public String toString()
    {
        return "ProgressStatus{" +
                "originalEstimate=" + originalEstimate +
                ", remainingRestimate=" + remainingRestimate +
                ", timeSpent=" + timeSpent +
                '}';
    }
}
