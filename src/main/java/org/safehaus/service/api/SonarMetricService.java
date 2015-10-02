package org.safehaus.service.api;


import org.safehaus.dao.entities.sonar.SonarMetricIssue;


/**
 * Created by ermek on 10/1/15.
 */
public interface SonarMetricService
{
    void insertSonarMetricIssue( SonarMetricIssue sonarMetricIssue );
}
