package org.safehaus.analysis.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.sonar.client.SonarManagerImpl;

/**
 * Created by kisik on 30.07.2015.
 */
public class SonarConnectorImpl implements SonarConnector {
    private static final Log log = LogFactory.getLog(SonarConnectorImpl.class);

    private String sonarURL;
    private String sonarUserName;
    private String sonarPass;

    public SonarConnectorImpl(String sonarURL, String sonarUserName, String sonarPass) {
        this.sonarURL = sonarURL;
        this.sonarUserName = sonarUserName;
        this.sonarPass = sonarPass;
    }

    public SonarManager sonarConnect()  throws SonarManagerException {
        log.info("sonarConnect()");
        SonarManager sonarMan = new SonarManagerImpl(sonarURL, sonarUserName, sonarPass);
        return sonarMan;
    }

}
