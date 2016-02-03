package org.safehaus.service.api;


import java.util.List;

import org.safehaus.dao.entities.stash.StashMetricIssue;


public interface StashMetricService
{
	void insertStashMetricIssue( StashMetricIssue stashMetricIssue );

	StashMetricIssue findStashMetricIssueById( String id );

	List<StashMetricIssue> findStashMetricIssuesByProjectName( String projectName );

	List<StashMetricIssue> getStashMetricsByProjectKey( String projectKey );

	List<StashMetricIssue> getStashMetricIssuesByAuthor( String authorId );

	List<StashMetricIssue> getStashMetricIssuesByUsername( String username, int limit );

	List<StashMetricIssue> getStashMetricIssuesByAuthorTimestamp( String timestamp );

	void updateStashMetricIssue( StashMetricIssue stashMetricIssue );

	void deleteStashMetricIssue( StashMetricIssue stashMetricIssue );

	int batchInsert( List<StashMetricIssue> issues );
}
