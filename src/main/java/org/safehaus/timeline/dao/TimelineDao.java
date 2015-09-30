package org.safehaus.timeline.dao;


import java.util.List;

import org.safehaus.timeline.model.StructuredIssue;
import org.safehaus.timeline.model.StructuredProject;


/**
 * Created by talas on 9/29/15.
 */
public interface TimelineDao
{
    public void insertProject( StructuredProject project );


    public void updateProject( StructuredProject project );


    public void deleteProject( StructuredProject project );


    public StructuredProject getProject( String id );


    public StructuredProject getProjectByKey( String projectKey );


    public List<StructuredProject> getAllProjects();


    public void insertStructuredIssue( StructuredIssue issue );


    public void updateStructuredIssue( StructuredIssue issue );


    public void deleteStructuredIssue( StructuredIssue issue );


    public StructuredIssue getStructuredIssue( String id );


    public StructuredIssue getStructuredIssueByKey( String key );


    public List<StructuredIssue> getAllStructuredISsues();
}
