package org.safehaus.service;


import java.util.List;

import org.safehaus.stash.model.StashMetricIssue;


public interface StashMetricService
{
	void insertStashMetricIssue( StashMetricIssue stashMetricIssue );

	StashMetricIssue findStashMetricIssueById( String id );

	List<StashMetricIssue> findStashMetricIssuesByProjectName( String projectName );

	List<StashMetricIssue> getStashMetricsByProjectKey( String projectKey );

	List<StashMetricIssue> getStashMetricIssuesByAuthor( String authorId );

	List<StashMetricIssue> getStashMetricIssuesByAuthorTimestamp( String timestamp );

	void updateStashMetricIssue( StashMetricIssue stashMetricIssue );

	void deleteStashMetricIssue( StashMetricIssue stashMetricIssue );

	void batchInsert( List<StashMetricIssue> issues );
}
