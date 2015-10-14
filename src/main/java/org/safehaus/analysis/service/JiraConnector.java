package org.safehaus.analysis.service;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraRestClient;

import net.rcarz.jiraclient.JiraClient;


/**
 * Created by kisik on 29.07.2015.
 */
public interface JiraConnector {
    JiraRestClient jiraConnect() throws JiraClientException;

    JiraClient getJiraClient() throws JiraClientException;

    void destroy();
}
