package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class FileInRevision
{
    private String projectId;
    private String revisionId;
    private String fileName;


    public String getProjectId()
    {
        return projectId;
    }


    public String getRevisionId()
    {
        return revisionId;
    }


    public String getFileName()
    {
        return fileName;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "projectId", projectId ).add( "revisionId", revisionId )
                      .add( "fileName", fileName ).toString();
    }
}
