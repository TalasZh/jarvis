package org.safehaus.service;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Created by talas on 10/13/15.
 */
public class QuartzInitiator implements ServletContextListener
{

    private static final Logger logger = LoggerFactory.getLogger( QuartzInitiator.class );


    public void contextInitialized( ServletContextEvent servletContextEvent )
    {
        WebApplicationContextUtils.getRequiredWebApplicationContext( servletContextEvent.getServletContext() )
                                  .getAutowireCapableBeanFactory().autowireBean( this );
        //your logic
        logger.info( "Application initialized." );
    }


    @Override
    public void contextDestroyed( final ServletContextEvent sce )
    {

    }
}
