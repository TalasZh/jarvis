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
import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.service.api.JiraMetricDao;


@RunWith( MockitoJUnitRunner.class )
public class JiraMetricDaoImplTest
{
	@Mock
	private Dao dao;

	@InjectMocks
	private JiraMetricDao jiraMetricDao = new JiraMetricDaoImpl();


	@Test
	public void testInsertJiraMetricIssue()
	{
		JiraMetricIssue jiraMetricIssue = new JiraMetricIssue();
		jiraMetricIssue.setIssueId( -2L );

		jiraMetricDao.insertJiraMetricIssue( jiraMetricIssue );

		Mockito.verify( dao ).insert( jiraMetricIssue );
	}


	@Test
	public void testFindJiraMetricIssueById()
	{
		JiraMetricIssue jiraMetricIssue = new JiraMetricIssue();
		jiraMetricIssue.setIssueId( -2L );
		jiraMetricIssue.setAssigneeName( "Test" );

		Mockito.when( dao.findById( JiraMetricIssue.class, -2L ) ).thenReturn( jiraMetricIssue );

		JiraMetricIssue newIssue = jiraMetricDao.getJiraMetricIssueById( -2L );

		Assert.assertNotNull( newIssue );
		Assert.assertEquals( (long) newIssue.getIssueId(), -2L );
		Assert.assertEquals( newIssue.getAssigneeName(), "Test" );

		Mockito.verify( dao ).findById( JiraMetricIssue.class, -2L );
	}

	@Test
	public void testFindJiraMetricIssuesByÅssigneeName()
	{
		List<JiraMetricIssue> issueList = new ArrayList<JiraMetricIssue>();
		issueList.add( new JiraMetricIssue() );
		issueList.add( new JiraMetricIssue() );

		Mockito.doReturn( issueList ).when( dao ).findByQuery( Matchers.anyString() );

		List<JiraMetricIssue> newList = jiraMetricDao.findJiraMetricIssuesByAssigneeName( "ttest" );

		Assert.assertNotNull( newList );
		Assert.assertTrue( newList.size() > 0 );
		Assert.assertTrue( newList.size() == 2 );

		Mockito.verify( dao ).findByQuery( Matchers.anyString() );
	}


	@Test
	public void testUpdateJiraMetricIssue()
	{
		JiraMetricIssue issue = new JiraMetricIssue();
		issue.setIssueId( -2L );
		jiraMetricDao.updateJiraMetricIssue( issue );

		Mockito.verify( dao ).merge( Matchers.any() );
	}


	@Test( expected = IllegalArgumentException.class )
	public void testUpdateJiraMetricIssueException()
	{
		jiraMetricDao.updateJiraMetricIssue( null );
	}


	@Test
	public void testDeleteJiraMetricIssue()
	{
		JiraMetricIssue issue = new JiraMetricIssue();
		issue.setIssueId( -2L );

		jiraMetricDao.deleteJiraMetricIssue( issue );

		Mockito.verify( dao ).remove( issue );
	}


	@Test( expected = IllegalArgumentException.class )
	public void testDeleteJiraMetricIssueException()
	{
		jiraMetricDao.deleteJiraMetricIssue( new JiraMetricIssue() );
	}

}
