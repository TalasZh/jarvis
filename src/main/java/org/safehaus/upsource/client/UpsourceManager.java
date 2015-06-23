package org.safehaus.upsource.client;


import java.util.Set;

import org.safehaus.upsource.model.Project;


public interface UpsourceManager
{
    public Set<Project> getAllProjects() throws UpsourceManagerException;

    public Project getProject( String projectId ) throws UpsourceManagerException;
}
