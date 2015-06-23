package org.safehaus.upsource.client;


import java.util.Set;

import org.safehaus.upsource.model.Project;
import org.safehaus.upsource.model.Revision;
import org.safehaus.upsource.model.RevisionDiffItem;


public interface UpsourceManager
{
    public Set<Project> getAllProjects() throws UpsourceManagerException;

    public Project getProject( String projectId ) throws UpsourceManagerException;

    public Set<Revision> getRevisions( String projectId, int limit ) throws UpsourceManagerException;

    public Revision getHeadRevision( String projectId ) throws UpsourceManagerException;

    public Set<Revision> getFilteredRevisions( String projectId, int limit, String revisionFilter )
            throws UpsourceManagerException;

    public Revision getRevision( String projectId, String revisionId ) throws UpsourceManagerException;

    public Set<RevisionDiffItem> getRevisionChanges( String projectId, String revisionId, String compareToRevisionId,
                                                int limit ) throws UpsourceManagerException;
}
