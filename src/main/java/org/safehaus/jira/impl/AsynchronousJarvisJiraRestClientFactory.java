package org.safehaus.jira.impl;


import java.net.URI;

import org.safehaus.jira.api.JarvisJiraRestClient;

import org.apache.http.cookie.Cookie;

import com.atlassian.httpclient.apache.httpcomponents.DefaultHttpClient;
import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.spi.ThreadLocalContextManagers;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public class AsynchronousJarvisJiraRestClientFactory implements JarvisJiraRestClientFactory
{

    @Override
    public JarvisJiraRestClient create( final URI serverUri, final AuthenticationHandler authenticationHandler )
    {
        final DisposableHttpClient httpClient =
                new AsynchronousHttpClientFactory().createClient( serverUri, authenticationHandler );
        return new AsynchronousJarvisJiraRestClient( serverUri, httpClient );
    }


    @Override
    public JarvisJiraRestClient createWithBasicHttpAuthentication( final URI serverUri, final String username,
                                                                   final String password )
    {
        return create( serverUri, new BasicHttpAuthenticationHandler( username, password ) );
    }


    @Override
    public JarvisJiraRestClient create( final URI serverUri, final HttpClient httpClient )
    {
        final DisposableHttpClient disposableHttpClient =
                new AsynchronousHttpClientFactory().createClient( httpClient );
        return new AsynchronousJarvisJiraRestClient( serverUri, disposableHttpClient );
    }


//    @Override
//    public JarvisJiraRestClient create( final URI uri, final Cookie cookie )
//    {
//
//        final DefaultHttpClient defaultHttpClient =
//                new DefaultHttpClient( new NoOpEventPublisher(), new RestClientApplicationProperties( serverUri ),
//                        ThreadLocalContextManagers.noop(), options );
//        final DisposableHttpClient disposableHttpClient =
//                new AsynchronousHttpClientFactory().createClient( httpClient );
//
//        return null;
//    }
}
