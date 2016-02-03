package org.safehaus.sonar.model;


import com.google.common.base.Objects;


public class QuantitativeStats
{
    public static final String LINES_OF_CODE_METRIC = "ncloc";
    public static final String LINES_METRIC = "lines";
    public static final String FILES_METRIC = "files";
    public static final String DIRECTORIES_METRIC = "directories";
    public static final String FUNCTIONS_METRIC = "functions";
    public static final String CLASSES_METRIC = "classes";
    public static final String STATEMENTS_METRIC = "statements";
    public static final String ACCESSORS_METRIC = "accessors";


    private double linesOfCode;
    private double lines;
    private double files;
    private double directories;
    private double functions;
    private double classes;
    private double statements;
    private double accessors;


    public QuantitativeStats( final double linesOfCode, final double lines, final double files,
                              final double directories, final double functions, final double classes,
                              final double statements, final double accessors )
    {
        this.linesOfCode = linesOfCode;
        this.lines = lines;
        this.files = files;
        this.directories = directories;
        this.functions = functions;
        this.classes = classes;
        this.statements = statements;
        this.accessors = accessors;
    }


    public double getLinesOfCode()
    {
        return linesOfCode;
    }


    public double getLines()
    {
        return lines;
    }


    public double getFiles()
    {
        return files;
    }


    public double getDirectories()
    {
        return directories;
    }


    public double getFunctions()
    {
        return functions;
    }


    public double getClasses()
    {
        return classes;
    }


    public double getStatements()
    {
        return statements;
    }


    public double getAccessors()
    {
        return accessors;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "linesOfCode", linesOfCode ).add( "lines", lines )
                      .add( "files", files ).add( "directories", directories ).add( "functions", functions )
                      .add( "classes", classes ).add( "statements", statements ).add( "accessors", accessors )
                      .toString();
    }
}
