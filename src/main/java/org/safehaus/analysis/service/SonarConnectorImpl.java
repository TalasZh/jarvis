package org.safehaus.analysis.service;


import org.safehaus.model.SonarContext;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.util.JarvisContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by kisik on 30.07.2015.
 */
public class SonarConnectorImpl implements SonarConnector
{
    private static final Logger logger = LoggerFactory.getLogger( SonarConnectorImpl.class );

    private String sonarURL;
    private String sonarUserName;
    private String sonarPass;


    public SonarConnectorImpl( String sonarURL, String sonarUserName, String sonarPass )
    {
        this.sonarURL = sonarURL;
        this.sonarUserName = sonarUserName;
        this.sonarPass = sonarPass;
    }


    public SonarManager sonarConnect() throws SonarManagerException
    {
        logger.info( "sonarConnect()" );

        SonarManager sonarManager = null;

        if ( JarvisContextHolder.getSonarContext() != null
                && JarvisContextHolder.getSonarContext().getSonarManager() != null )
        {
            sonarManager = JarvisContextHolder.getSonarContext().getSonarManager();
        }
        else
        {
            JarvisContextHolder.setSonarContext( new SonarContext( sonarURL, sonarUserName, sonarPass ) );
            if ( JarvisContextHolder.getStashContext() != null
                    && JarvisContextHolder.getStashContext().getStashClient() != null )
            {
                sonarManager = JarvisContextHolder.getSonarContext().getSonarManager();
            }
        }

        return sonarManager;
    }
}
