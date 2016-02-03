package org.safehaus.model;


import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * Created by tzhamakeev on 5/13/15.
 */
public enum SessionStatus
{
    OPEN( "Open" ), INPROGRESS( "In Progress" ), PAUSED( "Paused" ), CLOSED( "Closed" );

    private String value;


    SessionStatus( final String value )
    {
        this.value = value;
    }


    public String getValue()
    {
        return this.value;
    }

    @JsonValue
    @Override
    public String toString()
    {
        return value;
    }
}
