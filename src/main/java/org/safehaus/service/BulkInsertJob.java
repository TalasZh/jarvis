package org.safehaus.service;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * Created by talas on 10/13/15.
 */
public class BulkInsertJob extends QuartzJobBean
{
    private JiraPool jiraPool;
    private ConfluencePool confluencePool;


    public void setJiraPool( JiraPool jiraPool )
    {
        this.jiraPool = jiraPool;
    }


    public void setConfluencePool( final ConfluencePool confluencePool )
    {
        this.confluencePool = confluencePool;
    }


    @Override
    protected void executeInternal( final JobExecutionContext jobExecutionContext ) throws JobExecutionException
    {
        jiraPool.getJiraIssues();
        confluencePool.getConfluenceMetric();
    }
}
