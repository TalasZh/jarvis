package org.safehaus.jira.impl;


import java.net.URI;

import org.safehaus.jira.api.JarvisJiraRestClient;

import org.apache.http.cookie.Cookie;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public interface JarvisJiraRestClientFactory extends JiraRestClientFactory
{
    JarvisJiraRestClient create(URI var1, AuthenticationHandler var2);

    JarvisJiraRestClient createWithBasicHttpAuthentication(URI var1, String var2, String var3);

    JarvisJiraRestClient create(URI var1, HttpClient var2);

//    JarvisJiraRestClient create(URI uri, Cookie cookie);
}
