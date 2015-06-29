package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class Project
{
    private String projectName;
    private String projectId;
    private String headHash;
    private String codeReviewIdPattern;
    private long lastCommitDate;
    private String lastCommitAuthorName;
    private String projectModelType;


    public String getProjectName()
    {
        return projectName;
    }


    public String getProjectId()
    {
        return projectId;
    }


    public String getHeadHash()
    {
        return headHash;
    }


    public String getCodeReviewIdPattern()
    {
        return codeReviewIdPattern;
    }


    public long getLastCommitDate()
    {
        return lastCommitDate;
    }


    public String getLastCommitAuthorName()
    {
        return lastCommitAuthorName;
    }


    public String getProjectModelType()
    {
        return projectModelType;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "projectName", projectName ).add( "projectId", projectId )
                      .add( "headHash", headHash ).add( "codeReviewIdPattern", codeReviewIdPattern )
                      .add( "lastCommitDate", lastCommitDate ).add( "lastCommitAuthorName", lastCommitAuthorName )
                      .add( "projectModelType", projectModelType ).toString();
    }
}
