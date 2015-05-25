package org.safehaus.jira.api;


import org.safehaus.jira.impl.Group;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.util.concurrent.Promise;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public interface JarvisJiraRestClient extends JiraRestClient
{
    GroupRestClient getGroupClient();
}
