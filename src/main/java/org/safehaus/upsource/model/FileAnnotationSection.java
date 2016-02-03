package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class FileAnnotationSection
{
    private int startLine;
    private int lineCount;
    private Revision revision;
    private String filePath;


    public int getStartLine()
    {
        return startLine;
    }


    public int getLineCount()
    {
        return lineCount;
    }


    public Revision getRevision()
    {
        return revision;
    }


    public String getFilePath()
    {
        return filePath;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "startLine", startLine ).add( "lineCount", lineCount )
                      .add( "revision", revision ).add( "filePath", filePath ).toString();
    }
}
