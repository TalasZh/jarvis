package org.safehaus.sonar.model;


import java.util.Date;

import com.google.common.base.Objects;


public class TimeDuplicationStats extends DuplicationStats
{
    private Date date;


    public TimeDuplicationStats( final double duplicationPercent, final double duplicatedLines,
                                 final double duplicatedBlocks, final double duplicatedFiles, final Date date )
    {
        super( duplicationPercent, duplicatedLines, duplicatedBlocks, duplicatedFiles );
        this.date = date;
    }


    public Date getDate()
    {
        return date;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "duplicationPercent", getDuplicationPercent() )
                      .add( "duplicatedLines", getDuplicatedLines() ).add( "duplicatedBlocks", getDuplicatedBlocks() )
                      .add( "duplicatedFiles", getDuplicatedFiles() ).add( "date", getDate() ).toString();
    }
}
