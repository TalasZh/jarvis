package org.safehaus.analysis.service;


import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.client.ConfluenceManagerException;
import org.safehaus.confluence.client.ConfluenceManagerImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by kisik on 06.08.2015.
 */
public class ConfluenceConnectorImpl implements ConfluenceConnector
{

    private static final Log log = LogFactory.getLog( ConfluenceConnectorImpl.class );
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
        log.info( "confluenceConnect()" );
        ConfluenceManagerImpl confluenceManager =
                new ConfluenceManagerImpl( confluenceURL, confluenceUsername, confluencePass );
        return confluenceManager;
    }
}
