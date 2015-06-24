package org.safehaus.upsource.client;


import java.util.Set;

import org.safehaus.upsource.model.FileAnnotation;
import org.safehaus.upsource.model.FileHistory;
import org.safehaus.upsource.model.Project;
import org.safehaus.upsource.model.ReviewList;
import org.safehaus.upsource.model.Revision;
import org.safehaus.upsource.model.RevisionDiffItem;


/**
 * This manager provides means to execute a most commonly used subset of UpSource API. All methods are read-only, they
 * do not perform amy mutator operations on UpSource.
 *
 * See <a href="https://upsource.jetbrains.com/~api_doc/index.html">UpSource REST API</a>
 */
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

    public Set<String> getRevisionBranches( String projectId, String revisionId ) throws UpsourceManagerException;

    public FileAnnotation getFileAnnotation( String projectId, String revisionId, String fileName )
            throws UpsourceManagerException;

    public Set<String> getFileContributors( String projectId, String revisionId, String fileName )
            throws UpsourceManagerException;

    public FileHistory getFileHistory( String projectId, String revisionId, String fileName )
            throws UpsourceManagerException;

    public ReviewList getReviews( String projectId, String query, int limit ) throws UpsourceManagerException;
}
