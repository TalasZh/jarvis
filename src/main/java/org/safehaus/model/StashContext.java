package org.safehaus.model;


import org.safehaus.stash.client.StashClient;
import org.safehaus.stash.client.StashClientImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by talas on 10/13/15.
 */
public class StashContext
{
    private static final Logger logger = LoggerFactory.getLogger( StashContext.class );

    private String stashUrl;
    private String username;
    private String password;
    private StashClient stashClient;


    public StashContext( final String stashUrl, String username, String password )
    {
        this.stashUrl = stashUrl;
        this.username = username;
        this.password = password;
    }


    public StashClient getStashClient()
    {
        if ( stashClient == null )
        {
            logger.info( "Crearting new StashClient for {}", stashUrl );
            stashClient = new StashClientImpl( stashUrl, username, password );
        }
        return stashClient;
    }
}
