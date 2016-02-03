package org.safehaus.sonar.model;


import java.util.Date;

import com.google.common.base.Objects;


public class TimeQuantitativeStats extends QuantitativeStats
{
    private Date date;


    public TimeQuantitativeStats( final double linesOfCode, final double lines, final double files,
                                  final double directories, final double functions, final double classes,
                                  final double statements, final double accessors, final Date date )
    {
        super( linesOfCode, lines, files, directories, functions, classes, statements, accessors );
        this.date = date;
    }


    public Date getDate()
    {
        return date;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "linesOfCode", getLinesOfCode() ).add( "lines", getLines() )
                      .add( "files", getFiles() ).add( "directories", getDirectories() )
                      .add( "functions", getFunctions() ).add( "classes", getClasses() )
                      .add( "statements", getStatements() ).add( "accessors", getAccessors() ).add( "date", getDate() )
                      .toString();
    }
}
