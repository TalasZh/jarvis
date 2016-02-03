package org.safehaus.upsource.model;


import com.google.gson.annotations.SerializedName;


public enum ReviewCoverageStateEnum
{
    @SerializedName( "1" )
    CLOSED( 1 ),
    @SerializedName( "2" )
    OPEN( 2 ),
    @SerializedName( "3" )
    ALL( 3 );

    private int value;


    ReviewCoverageStateEnum( final int value )
    {
        this.value = value;
    }


    public int getValue()
    {
        return value;
    }
}
