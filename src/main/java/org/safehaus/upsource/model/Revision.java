package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class Revision
{
    private String revisionId;
    private long revisionDate;
    private long effectiveRevisionDate;
    private String revisionCommitMessage;
    private int state;
    private String revisionIdShort;
    private String revisionProblemMessage;
    private String authorId;
    private Set<String> branchHeadLabel;
    private Set<String> parentRevisions;
    private Set<String> childRevisions;
    private Set<String> tags;


    public String getRevisionId()
    {
        return revisionId;
    }


    public String getRevisionProblemMessage()
    {
        return revisionProblemMessage;
    }


    public Set<String> getChildRevisions()
    {
        return childRevisions;
    }


    public Set<String> getTags()
    {
        return tags;
    }


    public long getRevisionDate()
    {
        return revisionDate;
    }


    public long getEffectiveRevisionDate()
    {
        return effectiveRevisionDate;
    }


    public String getRevisionCommitMessage()
    {
        return revisionCommitMessage;
    }


    public int getState()
    {
        return state;
    }


    public String getRevisionIdShort()
    {
        return revisionIdShort;
    }


    public String getAuthorId()
    {
        return authorId;
    }


    public Set<String> getBranchHeadLabel()
    {
        return branchHeadLabel;
    }


    public Set<String> getParentRevisions()
    {
        return parentRevisions;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "revisionId", revisionId ).add( "revisionDate", revisionDate )
                      .add( "effectiveRevisionDate", effectiveRevisionDate )
                      .add( "revisionCommitMessage", revisionCommitMessage ).add( "state", state )
                      .add( "revisionIdShort", revisionIdShort ).add( "revisionProblemMessage", revisionProblemMessage )
                      .add( "authorId", authorId ).add( "branchHeadLabel", branchHeadLabel )
                      .add( "parentRevisions", parentRevisions ).add( "childRevisions", childRevisions )
                      .add( "tags", tags ).toString();
    }
}
