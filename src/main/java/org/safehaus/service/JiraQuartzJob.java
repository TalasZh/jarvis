package org.safehaus.service;


import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;


/**
 * Created by tzhamakeev on 5/14/15.
 */
public class JiraQuartzJob extends QuartzJobBean
{
    private JiraTask jiraTask;


    public void setJiraTask( JiraTask jiraTask )
    {
        this.jiraTask = jiraTask;
    }


    @Override
    protected void executeInternal( final JobExecutionContext jobExecutionContext ) throws JobExecutionException
    {
        jiraTask.run();
    }
}
