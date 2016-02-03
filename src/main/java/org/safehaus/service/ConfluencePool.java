package org.safehaus.service;


import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.safehaus.analysis.service.ConfluenceConnector;
import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.client.ConfluenceManagerException;
import org.safehaus.confluence.model.ConfluenceMetric;
import org.safehaus.confluence.model.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by talas on 10/14/15.
 */
public class ConfluencePool
{
    private static final Logger logger = LoggerFactory.getLogger( ConfluencePool.class );

    @Autowired
    private ConfluenceConnector confluenceConnector;


    public void getConfluenceMetric()
    {
        logger.info( "getConfluenceMetric" );
        ConfluenceManager confluenceManager;

        try
        {
            confluenceManager = confluenceConnector.confluenceConnect();
        }
        catch ( Exception e )
        {
            logger.error( "Couldn't connect to confluence", e );
            return;
        }

        List<Space> spaceList = null;
        try
        {
            spaceList = confluenceManager.getAllSpaces();
        }
        catch ( ConfluenceManagerException e )
        {
            logger.error( "Confluence Manager Exception ", e );
        }
        List<org.safehaus.confluence.model.Page> pageList = new ArrayList<>();
        if ( spaceList != null )
        {
            for ( Space s : spaceList )
            {
                try
                {
                    pageList.addAll                                                                                (
                            confluenceManager.listPagesWithOptions( s.getKey(), 0, 100, false, true, true, false ) );
                }
                catch ( ConfluenceManagerException e )
                {
                    logger.error( "Confluence Manager Exception ", e );
                }
            }
        }

        for ( org.safehaus.confluence.model.Page p : pageList )
        {
            ConfluenceMetric cf = new ConfluenceMetric();

            logger.info( "Page date: " + new DateTime( p.getVersion().getWhen() ).toDate() );

            cf.setAuthorDisplayName( p.getVersion().getBy().getDisplayName() );
            cf.setAuthorUserKey( p.getVersion().getBy().getUserKey() );
            cf.setAuthorUsername( p.getVersion().getBy().getUsername() );
            cf.setBodyLength( p.getBody().getView().getValue().length() );
            cf.setPageID( Integer.parseInt( p.getId() ) );
            cf.setTitle( p.getTitle() );
            cf.setVersionNumber( p.getVersion().getNumber() );
            cf.setWhen( new DateTime( p.getVersion().getWhen() ).toDate() );

            logger.info( "------------------------------------------------" );
            logger.info( "PageID:      " + cf.getPageID() );
            logger.info( "When:        " + cf.getWhen() );
            logger.info( "Number:      " + cf.getVersionNumber() );
            logger.info( "Username:    " + cf.getAuthorUsername() );
            logger.info( "Displayname: " + cf.getAuthorDisplayName() );
            logger.info( "UserKey:     " + cf.getAuthorUserKey() );
            logger.info( "BodyLen:     " + cf.getBodyLength() );
            logger.info( "Title:       " + cf.getTitle() );
        }
    }
}
