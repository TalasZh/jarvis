package org.safehaus.stash.util;


import java.util.List;

import javax.ws.rs.core.Cookie;

import org.junit.Test;
import org.safehaus.model.JarvisContext;
import org.safehaus.stash.model.Project;
import org.safehaus.util.JarvisContextHolder;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;


public class RestUtilTest
{
    protected static class Page<T>
    {
        List<T> values;


        public List<T> getValues()
        {
            return values;
        }
    }


    @Test
    public void testGet() throws Exception
    {
        JarvisContextHolder
                .setContext( new JarvisContext( "", new Cookie( "crowd.token_key", "xY33X50y7qPE5O6DqzsMNg00" ) ) );

        RestUtil restUtil = new RestUtil();

        String projectsResult = restUtil.get( "http://test-stash.critical-factor.com/rest/api/1.0/projects/",
                Maps.<String, String>newHashMap() );


        Page<Project> projects = JsonUtil.fromJson( projectsResult, new TypeToken<Page<Project>>()
        {}.getType() );


        for ( Project project : projects.getValues() )
        {
            System.out.println( project );
        }
    }
}
