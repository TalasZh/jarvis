package org.safehaus.webapp.action;


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


public class SonarAction extends BaseAction
{
    private static final String RESOURCE_ID = "org.safehaus.subutai:mgmt-parent";
    private SonarManager sonarManager;

    private UnitTestStats unitTestStats;
    private ComplexityStats complexityStats;
    private DuplicationStats duplicationStats;
    private QuantitativeStats quantitativeStats;
    private ViolationStats violationStats;


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


    public String list() throws JiraClientException
    {
        try
        {
            JarvisContextHolder.setContext( new JarvisContext( getSecurityCookie() ) );
            unitTestStats = sonarManager.getUnitTestStats( RESOURCE_ID );

            complexityStats = sonarManager.getComplexityStats( RESOURCE_ID );

            duplicationStats = sonarManager.getDuplicationStats( RESOURCE_ID );

            quantitativeStats = sonarManager.getQuantitativeStats( RESOURCE_ID );

            violationStats = sonarManager.getViolationStats( RESOURCE_ID );
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
