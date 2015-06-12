package org.safehaus.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;


/**
 * Created by tzhamakeev on 6/11/15.
 */
public class JarvisXmlMapper extends XmlMapper
{
    public JarvisXmlMapper()
    {
        super();
        JacksonXmlModule jacksonXmlModule = new JacksonXmlModule();
        jacksonXmlModule.addSerializer( new JiraTransitionSerializer() ); // assuming serializer declares correct class to bind to
        registerModule( jacksonXmlModule );
    }

}
