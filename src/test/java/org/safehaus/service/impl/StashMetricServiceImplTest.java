package org.safehaus.service.impl;


import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.safehaus.dao.Dao;
import org.safehaus.service.StashMetricService;
import org.safehaus.stash.model.StashMetricIssue;


@RunWith( MockitoJUnitRunner.class )
public class StashMetricServiceImplTest
{
	@Mock
	private Dao dao;

	@InjectMocks
	private StashMetricService stashMetricService = new StashMetricServiceImpl();


	@Test
	public void testInsertStashMetricIssue()
	{
		StashMetricIssue stashMetricIssue = new StashMetricIssue();
		stashMetricIssue.setId( -3L );

		stashMetricService.insertStashMetricIssue( stashMetricIssue );

		Mockito.verify( dao ).insert( stashMetricIssue );
	}


	@Test
	public void testFindStashMetricIssueById()
	{
		StashMetricIssue stashMetricIssue = new StashMetricIssue();
		Mockito.doReturn( stashMetricIssue ).when( dao ).findById( StashMetricIssue.class, -3L );
	}


	@Test
	public void testFindStashMetricIssuesByProjectName()
	{
		List<StashMetricIssue> stashIssues = new ArrayList<StashMetricIssue>();
		stashIssues.add( new StashMetricIssue() );
		stashIssues.add( new StashMetricIssue() );

		Mockito.doReturn( stashIssues ).when( dao ).findByQuery( Matchers.anyString() );

		List<StashMetricIssue> dbIssues = stashMetricService.findStashMetricIssuesByProjectName( "Jarvis" );

		Assert.assertNotNull( dbIssues );
		Assert.assertTrue( dbIssues.size() > 0 );
		Assert.assertTrue( dbIssues.size() == 2 );

		Mockito.verify( dao ).findByQuery( Matchers.anyString() );
	}


	@Test
	public void testUpdateStashMetricIssue()
	{
		StashMetricIssue stashMetricIssue = new StashMetricIssue();
		stashMetricIssue.setId( -3L );

		stashMetricService.updateStashMetricIssue( stashMetricIssue );

		Mockito.verify( dao ).merge( stashMetricIssue );
	}


	@Test( expected = IllegalArgumentException.class )
	public void testUpdateStashMetricIssueException()
	{
		stashMetricService.updateStashMetricIssue( null );
	}


	@Test
	public void testDeleteStashMetricIssue()
	{
		StashMetricIssue stashMetricIssue = new StashMetricIssue();
		stashMetricIssue.setId( -3L );

		stashMetricService.deleteStashMetricIssue( stashMetricIssue );

		Mockito.verify( dao ).remove( stashMetricIssue );
	}


	@Test( expected = IllegalArgumentException.class )
	public void testDeleteStashMetricIssueException()
	{
		stashMetricService.deleteStashMetricIssue( null );
	}
}
