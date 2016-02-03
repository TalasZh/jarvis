package org.safehaus.analysis.service;


import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.client.ConfluenceManagerException;
import org.safehaus.model.ConfluenceContext;
import org.safehaus.util.JarvisContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by kisik on 06.08.2015.
 */
public class ConfluenceConnectorImpl implements ConfluenceConnector
{
    private static final Logger logger = LoggerFactory.getLogger( ConfluenceConnectorImpl.class );

    private String confluenceURL;
    private String confluenceUsername;
    private String confluencePass;


    public ConfluenceConnectorImpl( String URL, String username, String pass )
    {
        this.confluenceURL = URL;
        this.confluenceUsername = username;
        this.confluencePass = pass;
    }


    @Override
    public ConfluenceManager confluenceConnect() throws ConfluenceManagerException
    {
        logger.info( "sonarConnect()" );

        ConfluenceManager sonarManager = null;

        if ( JarvisContextHolder.getConfluenceContext() != null
                && JarvisContextHolder.getConfluenceContext().getConfluenceManager() != null )
        {
            sonarManager = JarvisContextHolder.getConfluenceContext().getConfluenceManager();
        }
        else
        {
            JarvisContextHolder
                    .setConfluenceContext( new ConfluenceContext( confluenceURL, confluenceUsername, confluencePass ) );
            if ( JarvisContextHolder.getConfluenceContext() != null
                    && JarvisContextHolder.getConfluenceContext().getConfluenceManager() != null )
            {
                sonarManager = JarvisContextHolder.getConfluenceContext().getConfluenceManager();
            }
        }

        return sonarManager;
    }
}
