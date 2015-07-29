package org.safehaus.analysis.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraClient;
import org.safehaus.model.JarvisContext;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.sonar.client.SonarManagerImpl;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.stash.client.StashManagerImpl;
import org.safehaus.util.JarvisContextHolder;

/**
 * Created by kisik on 07.07.2015.
 */
public class AnalysisConnector  {
    private static final Log log = LogFactory.getLog(AnalysisConnector.class);

    public static JiraClient jiraConnect(String URL, String username, String pass) throws JiraClientException {
        log.info("jiraConnect()");
        JiraClient jiraClient = null;
        JarvisContextHolder.setContext(new JarvisContext(URL, username, pass));
        if(JarvisContextHolder.getContext() != null && JarvisContextHolder.getContext().getJiraClient() != null){
            jiraClient = JarvisContextHolder.getContext().getJiraClient();
        }
        else throw new JiraClientException("Jira Client is null.");
        return jiraClient;
    }

    public static SonarManager sonarConnect(String URL, String username, String pass) throws SonarManagerException {
        log.info("sonarConnect()");
        SonarManager sonarMan = new  SonarManagerImpl(URL, username, pass);
        return sonarMan;
    }

    public static StashManager stashConnect(String URL, String username, String pass) throws StashManagerException {
        log.info("stashConnect()");
        StashManager stashMan = null;
        stashMan = new StashManagerImpl(URL, username, pass);
        return stashMan;
    }

}
