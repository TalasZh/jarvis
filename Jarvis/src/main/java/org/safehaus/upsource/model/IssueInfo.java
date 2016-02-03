package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class IssueInfo
{
    private String issueId;
    private String issueLink;
    private String priority;
    private String type;
    private String state;
    private boolean isResolved;


    public String getIssueId()
    {
        return issueId;
    }


    public String getIssueLink()
    {
        return issueLink;
    }


    public String getPriority()
    {
        return priority;
    }


    public String getType()
    {
        return type;
    }


    public String getState()
    {
        return state;
    }


    public boolean isResolved()
    {
        return isResolved;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "issueId", issueId ).add( "issueLink", issueLink )
                      .add( "priority", priority ).add( "type", type ).add( "state", state )
                      .add( "isResolved", isResolved ).toString();
    }
}
