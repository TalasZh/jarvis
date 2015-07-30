package org.safehaus.analysis.service;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraClient;

/**
 * Created by kisik on 29.07.2015.
 */
public interface JiraConnector {
    JiraClient jiraConnect() throws JiraClientException;
}
