package org.safehaus.model;


import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.sonar.client.SonarManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by talas on 10/14/15.
 */
public class SonarContext
{
    private static final Logger logger = LoggerFactory.getLogger( SonarContext.class );

    private SonarManager sonarMan;
    private String sonarURL;
    private String sonarUserName;
    private String sonarPass;


    public SonarContext( String sonarURL, String sonarUserName, String sonarPass )
    {
        this.sonarURL = sonarURL;
        this.sonarUserName = sonarUserName;
        this.sonarPass = sonarPass;
    }


    public SonarManager getSonarManager() throws SonarManagerException
    {
        logger.info( "sonarConnect()" );
        if ( sonarMan == null )
        {
            logger.info( "Creating new sonar manager" );
            sonarMan = new SonarManagerImpl( sonarURL, sonarUserName, sonarPass );
        }
        return sonarMan;
    }


    public void destroy()
    {
        if ( sonarMan != null )
        {
            logger.info( "Destroying sonar manager" );
            sonarMan = null;
        }
    }
}
