package org.safehaus.sonar.model;


import java.util.Date;

import com.google.common.base.Objects;


public class TimeComplexityStats extends ComplexityStats
{
    private Date date;


    public TimeComplexityStats( final double complexity, final double fileComplexity, final double classComplexity,
                                final double functionComplexity, final Date date )
    {
        super( complexity, fileComplexity, classComplexity, functionComplexity );

        this.date = date;
    }


    public Date getDate()
    {
        return date;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "complexity", getComplexity() )
                      .add( "fileComplexity", getFileComplexity() ).add( "classComplexity", getClassComplexity() )
                      .add( "functionComplexity", getFunctionComplexity() ).add( "date", getDate() ).toString();
    }
}
