package org.safehaus.analysis.service;


import org.safehaus.model.StashContext;
import org.safehaus.stash.client.StashClient;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.util.JarvisContextHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by kisik on 29.07.2015.
 */
public class StashConnectorImpl implements StashConnector
{
    private static final Log log = LogFactory.getLog( StashConnectorImpl.class );

    private StashClient stashClient;

    private String stashURL;
    private String stashUserName;
    private String stashPass;


    StashConnectorImpl( String stashURL, String stashUserName, String stashPass )
    {
        this.stashURL = stashURL;
        this.stashUserName = stashUserName;
        this.stashPass = stashPass;
    }


    public StashClient stashConnect() throws StashManagerException
    {
        log.info( "stashConnect()" );
        stashClient = null;

        if ( JarvisContextHolder.getStashContext() != null
                && JarvisContextHolder.getStashContext().getStashClient() != null )
        {
            stashClient = JarvisContextHolder.getStashContext().getStashClient();
        }
        else
        {
            JarvisContextHolder.setStashContext( new StashContext( stashURL, stashUserName, stashPass ) );
            if ( JarvisContextHolder.getStashContext() != null
                    && JarvisContextHolder.getStashContext().getStashClient() != null )
            {
                stashClient = JarvisContextHolder.getStashContext().getStashClient();
            }
        }

        return stashClient;
    }
}
