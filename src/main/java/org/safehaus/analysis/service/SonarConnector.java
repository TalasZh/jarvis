package org.safehaus.analysis.service;

import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;

/**
 * Created by kisik on 29.07.2015.
 */
public interface SonarConnector {
    SonarManager sonarConnect()  throws SonarManagerException;
}
