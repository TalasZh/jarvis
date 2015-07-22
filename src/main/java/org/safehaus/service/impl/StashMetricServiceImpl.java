package org.safehaus.service.impl;


import java.util.List;
import org.safehaus.dao.Dao;
import org.safehaus.service.StashMetricService;
import org.safehaus.stash.model.StashMetricIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StashMetricServiceImpl implements StashMetricService
{
	private static final Logger log = LoggerFactory.getLogger( StashMetricServiceImpl.class );

	@Autowired
	private Dao dao;


	@Override
	public void insertStashMetricIssue( StashMetricIssue stashMetricIssue )
	{
		log.info( "Inserting new StashMetricIssue" );
		dao.insert( stashMetricIssue );
	}


	@Override
	public StashMetricIssue findStashMetricIssueById( Long id )
	{
		log.info( "Finding StashMetricIssue by id: {}", id );
		return dao.findById( StashMetricIssue.class, id );
	}


	@Override
	public List<StashMetricIssue> findStashMetricIssuesByProjectName( String projectName )
	{
		String query = "Select s from " + StashMetricIssue.class.getSimpleName() + " s where s.projectName = "
		        + projectName;

		log.info( "Finding StashMetricIssue by projectName : {}", projectName );
		List<StashMetricIssue> stashMetricIssues = (List<StashMetricIssue>) dao.findByQuery( query );
		return stashMetricIssues;
	}


	@Override
	public void updateStashMetricIssue( StashMetricIssue stashMetricIssue )
	{
		if ( stashMetricIssue == null )
		{
			throw new IllegalArgumentException( "Entity or Entity.id should not be null to perform update." );
		}

		log.info( "Updating StashMetricIssue with id {}", stashMetricIssue.getId() );
		dao.merge( stashMetricIssue );
	}


	@Override
	public void deleteStashMetricIssue( StashMetricIssue stashMetricIssue )
	{
		if ( stashMetricIssue == null )
		{
			throw new IllegalArgumentException( "Entity or Entity.id should not be null to perform delete." );
		}

		log.info( "Removing StashMetricIssue with id {}", stashMetricIssue.getId() );
		dao.remove( stashMetricIssue );
	}

}
