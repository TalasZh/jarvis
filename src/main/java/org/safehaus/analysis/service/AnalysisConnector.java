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

    public static JiraClient jiraConnect() throws JiraClientException {
        log.info("jiraConnect()");
        JarvisContextHolder.setContext(new JarvisContext("http://test-jira.critical-factor.com", "username", "password"));
        JiraClient jiraClient = JarvisContextHolder.getContext().getJiraClient();

        return jiraClient;
    }

    public static SonarManager sonarConnect() throws SonarManagerException {
        log.info("sonarConnect()");
        SonarManager sonarMan = new  SonarManagerImpl("http://sonar.subutai.io","sonar-bot", "7\"CV23xR2A#K3h");
        return sonarMan;
    }

    public static StashManager stashConnect() throws StashManagerException {
        log.info("stashConnect()");
        StashManager stashMan = new StashManagerImpl("http://stash.critical-factor.com", "username", "password");
        return stashMan;
    }

}
