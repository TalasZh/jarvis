package org.safehaus.stash.model;


import com.google.common.base.Objects;


public class BuildStatistics
{
    private int successful;
    private int inProgress;
    private int failed;


    public int getSuccessful()
    {
        return successful;
    }


    public int getInProgress()
    {
        return inProgress;
    }


    public int getFailed()
    {
        return failed;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "successful", successful ).add( "inProgress", inProgress )
                      .add( "failed", failed ).toString();
    }
}
