package io.subutai.domain;


import com.atlassian.crowd.embedded.api.User;


public class IssueWrapper
{
    private String issueKey;
    private String assignee;
    private String approvingUser;
    /*
    *  0 - indicates that current assignee is original assignee
    *  1 - indicates that current assignee is from approving group
    * */
    private int direction = 0;


    public int getDirection()
    {
        return direction;
    }


    public void setDirection( final int direction )
    {
        this.direction = direction;
    }


    public IssueWrapper( final String issueKey, final String assignee, final String approvingUser, final int direction )
    {
        this.issueKey = issueKey;
        this.assignee = assignee;
        this.approvingUser = approvingUser;
        this.direction = direction;
    }


    @Override
    public String toString()
    {
        return "IssueWrapper{" +
                "issueKey='" + issueKey + '\'' +
                ", assignee='" + assignee + '\'' +
                ", approvingUser='" + approvingUser + '\'' +
                ", direction=" + direction +
                '}';
    }


    public String getIssueKey()
    {
        return issueKey;
    }


    public void setIssueKey( final String issueKey )
    {
        this.issueKey = issueKey;
    }


    public String getAssignee()
    {
        return assignee;
    }


    public void setAssignee( final String assignee )
    {
        this.assignee = assignee;
    }


    public String getApprovingUser()
    {
        return approvingUser;
    }


    public void setApprovingUser( final String approvingUser )
    {
        this.approvingUser = approvingUser;
    }
}
