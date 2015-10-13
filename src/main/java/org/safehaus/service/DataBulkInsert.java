package org.safehaus.service;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * Created by talas on 10/13/15.
 */
public class DataBulkInsert extends QuartzJobBean
{
    private JiraPool jiraPool;


    public void setJiraPool( JiraPool jiraPool )
    {
        this.jiraPool = jiraPool;
    }


    @Override
    protected void executeInternal( final JobExecutionContext jobExecutionContext ) throws JobExecutionException
    {
        jiraPool.getJiraIssues();
    }
}
