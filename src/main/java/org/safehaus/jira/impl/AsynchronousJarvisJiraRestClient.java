package org.safehaus.jira.impl;


import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.safehaus.jira.api.GroupRestClient;
import org.safehaus.jira.api.JarvisJiraRestClient;

import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClient;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public class AsynchronousJarvisJiraRestClient extends AsynchronousJiraRestClient implements JarvisJiraRestClient
{
    private final GroupRestClient groupRestClient;


    public AsynchronousJarvisJiraRestClient( final URI serverUri, final DisposableHttpClient httpClient )
    {
        super( serverUri, httpClient );
        final URI baseUri = UriBuilder.fromUri( serverUri ).path("/rest/api/latest").build();
        this.groupRestClient = new AsynchronousGroupRestClient( baseUri, httpClient );
    }


    @Override
    public GroupRestClient getGroupClient()
    {
        return this.groupRestClient;
    }
}
