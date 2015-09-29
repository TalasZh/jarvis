package org.safehaus.timeline.rest;


import java.util.List;

import javax.jws.WebService;

import org.safehaus.timeline.TimelineManager;
import org.safehaus.timeline.model.StoryTimeline;
import org.safehaus.timeline.model.StructuredProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by talas on 9/27/15.
 */
@Service( "timelineRestServiceImpl" )
@WebService( serviceName = "TimelineRestServiceImpl",
        endpointInterface = "org.safehaus.timeline.rest.TimelineRestService" )
public class TimelineRestServiceImpl implements TimelineRestService
{

    private static final Logger logger = LoggerFactory.getLogger( TimelineRestServiceImpl.class );

    @Autowired( required = true )
    private TimelineManager timelineManager;


    /**
     * Constructs project dependency tree
     *
     * @param projectKey - target project key to view dependency
     *
     * @return - project
     */
    @Override
    public StructuredProject getProject( final String projectKey )
    {
        return timelineManager.getProject( projectKey );
    }


    /**
     * returns list of projects
     */
    @Override
    public List<StructuredProject> getProjects()
    {
        return timelineManager.getProjects();
    }


    /**
     * constructs story timeline according to dependency in issues
     */
    @Override
    public StoryTimeline getStoryTimeline( final String storyKey, final String fromDate, final String toDate )
    {
        return timelineManager.getStoryTimeline( storyKey, fromDate, toDate );
    }
}
