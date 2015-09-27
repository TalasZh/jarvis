package org.safehaus.timeline.rest;


import java.util.List;

import org.codehaus.jackson.map.annotate.JsonView;
import org.safehaus.model.Views;
import org.safehaus.service.api.JiraMetricService;
import org.safehaus.timeline.StructuredProject;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by talas on 9/27/15.
 */
public class TimelineRestServiceImpl implements TimelineRestService
{
    @Autowired
    private JiraMetricService jiraMetricService;


    @Override
    @JsonView( Views.TimelineLong.class )
    public StructuredProject getProject( final String projectKey )
    {
        return null;
    }


    @Override
    @JsonView( Views.TimelineShort.class )
    public List<StructuredProject> getProjects()
    {
        return null;
    }
}
