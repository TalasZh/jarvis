package org.safehaus.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by kisik on 30.06.2015.
 */
public class AnalysisServiceQuartzJob extends QuartzJobBean {

    private AnalysisService analysisService;

    public void setAnalysisService(AnalysisService analysisService)
    {
        this.analysisService = analysisService;
    }

    @Override
    protected void executeInternal( final JobExecutionContext jobExecutionContext ) throws JobExecutionException {
        analysisService.run();
    }

}
