package org.safehaus.jira.impl;


import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;


/**
 * Created by tzhamakeev on 5/26/15.
 */
public class CrowdAuthenticationHandler implements AuthenticationHandler
{
    private static Logger logger = LoggerFactory.getLogger( CrowdAuthenticationHandler.class );

    private String crowdToken;


    public CrowdAuthenticationHandler( final String crowdToken )
    {
        this.crowdToken = crowdToken;
    }


    @Override
    public void configure( final Request request )
    {
        logger.debug( "Configuring CrowdAuthenticationHandler with token: " + crowdToken );
        request.setHeader( "Cookie", String.format( "crowd.token_key=%s", crowdToken ) );
    }
}
