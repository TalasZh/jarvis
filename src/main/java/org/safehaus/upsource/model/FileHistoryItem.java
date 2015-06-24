package org.safehaus.upsource.model;


import com.google.common.base.Objects;


public class FileHistoryItem
{
    private int diffType;
    private Revision revision;
    private String fileName;


    public int getDiffType()
    {
        return diffType;
    }


    public Revision getRevision()
    {
        return revision;
    }


    public String getFileName()
    {
        return fileName;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "diffType", diffType ).add( "revision", revision )
                      .add( "fileName", fileName ).toString();
    }
}
