package org.safehaus.sonar.model;


import com.google.common.base.Objects;


public class ComplexityStats
{
    public static final String COMPLEXITY_METRIC = "complexity";
    public static final String FILE_COMPLEXITY_METRIC = "file_complexity";
    public static final String CLASS_COMPLEXITY_METRIC = "class_complexity";
    public static final String FUNCTION_COMPLEXITY_METRIC = "function_complexity";

    private double complexity;
    private double fileComplexity;
    private double classComplexity;
    private double functionComplexity;


    public ComplexityStats( final double complexity, final double fileComplexity, final double classComplexity,
                            final double functionComplexity )
    {
        this.complexity = complexity;
        this.fileComplexity = fileComplexity;
        this.classComplexity = classComplexity;
        this.functionComplexity = functionComplexity;
    }


    public double getComplexity()
    {
        return complexity;
    }


    public double getFileComplexity()
    {
        return fileComplexity;
    }


    public double getClassComplexity()
    {
        return classComplexity;
    }


    public double getFunctionComplexity()
    {
        return functionComplexity;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "complexity", complexity ).add( "fileComplexity", fileComplexity )
                      .add( "classComplexity", classComplexity ).add( "functionComplexity", functionComplexity )
                      .toString();
    }
}
