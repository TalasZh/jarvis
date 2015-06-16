package org.safehaus.sonar.model;


import com.google.common.base.Objects;


public class DuplicationStats
{
    public static final String DUPLICATION_PERCENT_METRIC = "duplicated_lines_density";
    public static final String DUPLICATED_LINES_METRIC = "duplicated_lines";
    public static final String DUPLICATED_BLOCKS_METRIC = "duplicated_blocks";
    public static final String DUPLICATED_FILES_METRIC = "duplicated_files";

    private double duplicationPercent;
    private double duplicatedLines;
    private double duplicatedBlocks;
    private double duplicatedFiles;


    public DuplicationStats( final double duplicationPercent, final double duplicatedLines,
                             final double duplicatedBlocks, final double duplicatedFiles )
    {
        this.duplicationPercent = duplicationPercent;
        this.duplicatedLines = duplicatedLines;
        this.duplicatedBlocks = duplicatedBlocks;
        this.duplicatedFiles = duplicatedFiles;
    }


    public double getDuplicationPercent()
    {
        return duplicationPercent;
    }


    public double getDuplicatedLines()
    {
        return duplicatedLines;
    }


    public double getDuplicatedBlocks()
    {
        return duplicatedBlocks;
    }


    public double getDuplicatedFiles()
    {
        return duplicatedFiles;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "duplicationPercent", duplicationPercent )
                      .add( "duplicatedLines", duplicatedLines ).add( "duplicatedBlocks", duplicatedBlocks )
                      .add( "duplicatedFiles", duplicatedFiles ).toString();
    }
}
