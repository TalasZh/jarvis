package org.safehaus.timeline.rest;


import java.util.List;

import org.safehaus.timeline.StructuredProject;


/**
 * Created by talas on 9/27/15.
 */
public interface TimelineRestService
{
    public StructuredProject getProject( String projectKey );

    public List<StructuredProject> getProjects();
}
