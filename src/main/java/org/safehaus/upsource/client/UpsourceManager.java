package org.safehaus.upsource.client;


import java.util.Set;

import org.safehaus.upsource.model.Project;
import org.safehaus.upsource.model.Revision;


public interface UpsourceManager
{
    public Set<Project> getAllProjects() throws UpsourceManagerException;

    public Project getProject( String projectId ) throws UpsourceManagerException;

    public Set<Revision> getRevisions( String projectId, int limit ) throws UpsourceManagerException;

    public Revision getHeadRevision( String projectId ) throws UpsourceManagerException;

    public Set<Revision> getFilteredRevisions( String projectId, int limit, String revisionFilter )
            throws UpsourceManagerException;
}
