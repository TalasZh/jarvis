package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class RevisionDiffItem
{
    private String projectId;
    private int diffType;
    private FileInRevision newFile;
    private FileInRevision oldFile;
    private String fileIcon;


    public String getProjectId()
    {
        return projectId;
    }


    public int getDiffType()
    {
        return diffType;
    }


    public FileInRevision getNewFile()
    {
        return newFile;
    }


    public FileInRevision getOldFile()
    {
        return oldFile;
    }


    public String getFileIcon()
    {
        return fileIcon;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "projectId", projectId ).add( "diffType", diffType )
                      .add( "newFile", newFile ).add( "oldFile", oldFile ).add( "fileIcon", fileIcon ).toString();
    }
}
