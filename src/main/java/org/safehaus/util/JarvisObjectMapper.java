package org.safehaus.util;


import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


/**
 * Created by tzhamakeev on 6/11/15.
 */
public class JarvisObjectMapper extends ObjectMapper
{
    public JarvisObjectMapper()
    {
        super();
        SimpleModule testModule = new SimpleModule( "JarvisModule" );
        testModule.addSerializer(
                new JiraTransitionSerializer() ); // assuming serializer declares correct class to bind to
        registerModule( testModule );
    }
}
