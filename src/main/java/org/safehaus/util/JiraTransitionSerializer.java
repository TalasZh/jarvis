package org.safehaus.util;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.domain.Transition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


/**
 * Created by tzhamakeev on 6/11/15.
 */
public class JiraTransitionSerializer extends StdSerializer<Transition>
{
    private static Logger logger = LoggerFactory.getLogger( JiraTransitionSerializer.class );


    public JiraTransitionSerializer()
    {
        super( Transition.class );
    }


    @Override
    public void serialize( final Transition transition, final JsonGenerator jsonGenerator,
                           final SerializerProvider serializerProvider ) throws IOException, JsonProcessingException
    {
        logger.debug( serializerProvider.getActiveView() != null ? serializerProvider.getActiveView().getName() :
                      "View not found" );
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField( "id", transition.getId() );
        jsonGenerator.writeStringField( "name", transition.getName() );
        jsonGenerator.writeEndObject();
    }
}
