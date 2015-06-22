package org.safehaus.webapp.action;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.JarvisContext;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.model.ComplexityStats;
import org.safehaus.sonar.model.DuplicationStats;
import org.safehaus.sonar.model.QuantitativeStats;
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;
import org.safehaus.stash.client.Page;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.model.Project;
import org.safehaus.util.JarvisContextHolder;
import org.sonar.wsclient.services.Resource;


public class SonarAction extends BaseAction
{
    private static final String RESOURCE_ID = "org.safehaus.subutai:mgmt-parent";
    private SonarManager sonarManager;

    private UnitTestStats unitTestStats;
    private ComplexityStats complexityStats;
    private DuplicationStats duplicationStats;
    private QuantitativeStats quantitativeStats;
    private ViolationStats violationStats;
    private Set<Resource> resources;
    private String project;


    public void setSonarManager( final SonarManager sonarManager )
    {
        this.sonarManager = sonarManager;
    }


    public UnitTestStats getUnitTestStats()
    {
        return unitTestStats;
    }


    public ComplexityStats getComplexityStats()
    {
        return complexityStats;
    }


    public DuplicationStats getDuplicationStats()
    {
        return duplicationStats;
    }


    public QuantitativeStats getQuantitativeStats()
    {
        return quantitativeStats;
    }


    public ViolationStats getViolationStats()
    {
        return violationStats;
    }


    public Set<Resource> getResources()
    {
        return resources;
    }


    public String getProject()
    {
        return project;
    }


    public void setProject( final String project )
    {
        this.project = project;
    }


    public String list() throws JiraClientException
    {
        log.debug( project );

        if ( this.cancel != null )
        {
            return "cancel";
        }


        try
        {
            JarvisContextHolder.setContext( new JarvisContext( getSecurityCookie() ) );

            resources = sonarManager.getResources();

            if ( project == null )
            {
                return SUCCESS;
            }

            unitTestStats = sonarManager.getUnitTestStats( project );

            complexityStats = sonarManager.getComplexityStats( project );

            duplicationStats = sonarManager.getDuplicationStats( project );

            quantitativeStats = sonarManager.getQuantitativeStats( project );

            violationStats = sonarManager.getViolationStats( project );
        }
        catch ( Exception e )
        {
            log.error( e.toString(), e );
        }
        finally
        {
            JarvisContextHolder.getContext().destroy();
        }
        return SUCCESS;
    }
}
