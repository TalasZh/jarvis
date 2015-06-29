package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class AnalyzerStats
{
    private long minCommitTime;
    private long maxCommitTime;
    private int totalCommits;
    private long minIndexedCommitTime;
    private long maxIndexedCommitTime;
    private int totalIndexedCommits;
    private boolean projectModelKnown;


    public long getMinCommitTime()
    {
        return minCommitTime;
    }


    public long getMaxCommitTime()
    {
        return maxCommitTime;
    }


    public int getTotalCommits()
    {
        return totalCommits;
    }


    public long getMinIndexedCommitTime()
    {
        return minIndexedCommitTime;
    }


    public long getMaxIndexedCommitTime()
    {
        return maxIndexedCommitTime;
    }


    public int getTotalIndexedCommits()
    {
        return totalIndexedCommits;
    }


    public boolean isProjectModelKnown()
    {
        return projectModelKnown;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "minCommitTime", minCommitTime )
                      .add( "maxCommitTime", maxCommitTime ).add( "totalCommits", totalCommits )
                      .add( "minIndexedCommitTime", minIndexedCommitTime )
                      .add( "maxIndexedCommitTime", maxIndexedCommitTime )
                      .add( "totalIndexedCommits", totalIndexedCommits ).add( "projectModelKnown", projectModelKnown )
                      .toString();
    }
}
