package org.safehaus.upsource.model;


import com.google.gson.annotations.SerializedName;


public enum TimeUnitEnum
{
    @SerializedName( "1" )
    HOUR( 1 ),
    @SerializedName( "2" )
    DAY( 2 ),
    @SerializedName( "3" )
    WEEK( 3 ),
    @SerializedName( "4" )
    MONTH( 4 ),
    @SerializedName( "5" )
    QUARTER( 5 ),
    @SerializedName( "6" )
    YEAR( 6 );


    private final int value;


    TimeUnitEnum( int value )
    {
        this.value = value;
    }


    public int getValue()
    {
        return value;
    }
}

