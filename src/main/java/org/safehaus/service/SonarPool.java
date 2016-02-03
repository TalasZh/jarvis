package org.safehaus.service;


import java.util.Set;

import org.safehaus.analysis.service.SonarConnector;
import org.safehaus.dao.entities.sonar.SonarMetricIssue;
import org.safehaus.service.api.SonarMetricService;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.sonar.model.QuantitativeStats;
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.wsclient.services.Resource;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by talas on 10/14/15.
 */
public class SonarPool
{
    private static final Logger logger = LoggerFactory.getLogger( SonarPool.class );

    @Autowired
    private SonarMetricService sonarMetricService;

    @Autowired
    private SonarConnector sonarConnector;


    public void getSonarMetrics()
    {
        SonarManager sonarManager;

        try
        {
            sonarManager = sonarConnector.sonarConnect();
        }
        catch ( SonarManagerException e )
        {
            logger.error( "Error connecting to sonar", e );
            return;
        }

        Set<Resource> resources = null;
        logger.info( "Get Sonar Metric Issues ici." );
        try
        {
            resources = sonarManager.getResources();
            if ( resources != null )
            {
                for ( Resource r : resources )
                {
                    SonarMetricIssue sonarMetricIssue = new SonarMetricIssue();
                    int projectId = r.getId();
                    String projectKey = r.getKey();
                    String projectName = r.getName();

                    UnitTestStats unitTestStats = sonarManager.getUnitTestStats( projectKey );
                    ViolationStats violationStats = sonarManager.getViolationStats( projectKey );
                    QuantitativeStats quantitativeStats = sonarManager.getQuantitativeStats( projectKey );

                    sonarMetricIssue.setProjectId( projectId );
                    sonarMetricIssue.setProjectName( projectName );
                    sonarMetricIssue.setSuccessPercent( unitTestStats.getSuccessPercent() );
                    sonarMetricIssue.setFailures( unitTestStats.getFailures() );
                    sonarMetricIssue.setErrors( unitTestStats.getErrors() );
                    sonarMetricIssue.setTestCount( unitTestStats.getFailures() );
                    sonarMetricIssue.setCoveragePercent( unitTestStats.getCoveragePercent() );
                    sonarMetricIssue.setAllIssues( violationStats.getAllIssues() );
                    sonarMetricIssue.setBlockerIssues( violationStats.getBlockerIssues() );
                    sonarMetricIssue.setCriticalIssues( violationStats.getCriticalIssues() );
                    sonarMetricIssue.setClassesCount( quantitativeStats.getClasses() );
                    sonarMetricIssue.setFunctionsCount( quantitativeStats.getFunctions() );
                    sonarMetricIssue.setFilesCount( quantitativeStats.getFiles() );
                    sonarMetricIssue.setMajorIssues( violationStats.getMajorIssues() );
                    sonarMetricIssue.setLinesOfCode( quantitativeStats.getLinesOfCode() );

                    sonarMetricService.insertSonarMetricIssue( sonarMetricIssue );
                }
            }
        }
        catch ( SonarManagerException e )
        {
            logger.error( "Error pulling sonar metrics", e );
        }
    }
}
