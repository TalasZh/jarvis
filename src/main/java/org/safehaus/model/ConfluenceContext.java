package org.safehaus.model;


import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.client.ConfluenceManagerException;
import org.safehaus.confluence.client.ConfluenceManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by talas on 10/14/15.
 */
public class ConfluenceContext
{
    private static final Logger logger = LoggerFactory.getLogger( ConfluenceContext.class );

    private String confluenceURL;
    private String confluenceUsername;
    private String confluencePass;
    private ConfluenceManager confluenceManager;


    public ConfluenceContext( String URL, String username, String pass )
    {
        this.confluenceURL = URL;
        this.confluenceUsername = username;
        this.confluencePass = pass;
    }


    public ConfluenceManager getConfluenceManager() throws ConfluenceManagerException
    {
        logger.info( "confluenceConnect()" );
        if ( confluenceManager == null )
        {
            logger.info( "Creating new confluence manager" );
            confluenceManager = new ConfluenceManagerImpl( confluenceURL, confluenceUsername, confluencePass );
        }
        return confluenceManager;
    }


    public void destroy()
    {
        if ( confluenceManager == null )
        {
            logger.info( "Destroying confluence manager" );
            confluenceManager = null;
        }
    }
}
