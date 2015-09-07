package org.safehaus.service;


import java.util.List;

import org.safehaus.stash.model.StashMetricIssue;


public interface StashMetricService
{
	void insertStashMetricIssue( StashMetricIssue stashMetricIssue );

	StashMetricIssue findStashMetricIssueById( String id );

	List<StashMetricIssue> findStashMetricIssuesByProjectName( String projectName );

	List<StashMetricIssue> getStashMetricsByProjectKey( String projectKey );

	List<StashMetricIssue> getStashMetricIssuesByAuthor( Long authorId );

	List<StashMetricIssue> getStashMetricIssuesByAuthorTimestamp( Long timestamp );

	void updateStashMetricIssue( StashMetricIssue stashMetricIssue );

	void deleteStashMetricIssue( StashMetricIssue stashMetricIssue );

	void batchInsert( List<StashMetricIssue> issues );
}
