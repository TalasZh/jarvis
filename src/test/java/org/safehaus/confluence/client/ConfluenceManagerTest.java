package org.safehaus.confluence.client;


import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Cookie;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.safehaus.confluence.model.LabelResult;
import org.safehaus.confluence.model.Page;
import org.safehaus.confluence.model.Space;
import org.safehaus.model.JarvisContext;
import org.safehaus.util.JarvisContextHolder;


@RunWith( MockitoJUnitRunner.class )
public class ConfluenceManagerTest
{

	private ConfluenceManager confluenceManager;


	@Before
	public void setup() throws Exception
	{
		// For integration test we setup existing cookie instead of writing
		// username password
		confluenceManager = new ConfluenceManagerImpl( "http://test-confluence.critical-factor.com", "aosmonov", "test" );

//		JarvisContextHolder.setContext( new JarvisContext( "", new Cookie( "seraph.confluence",
//		        "2260994%3Ad35c7e6616ebaeb413cb0b2d5b4e254786c67d19" ) ) );
	}


	@Test
	public void testClientTest()
	{
		Client client = new Client( Protocol.HTTP );
		Response response = client.handle( new Request( Method.GET,
		        "https://confluence.subutai.io/rest/api/content/8258143" ) );

		System.out.println( response.getStatus() );
		System.out.println( response.getEntityAsText() );

	}


	@Test
	public void testCreateNewSpace()
	{
		Space space = new Space();
		space.setKey( "TEST" );
		space.setName( "Test" );
		space.setType( "global" );

		space.setDescription( space.new Description() );
		space.getDescription().setPlain( space.getDescription().new Value() );
		space.getDescription().getPlain().setRepresentation( "plain" );
		space.getDescription().getPlain().setValue( "Test space for REST API." );

		try
		{
			Space aSpace = confluenceManager.createNewSpace( space );

			Assert.assertNotNull( aSpace );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testGetSpace()
	{
		try
		{
			Space space = confluenceManager.getSpace( "TEST" );

			Assert.assertNotNull( space );
			Assert.assertNotNull( space.getDescription() );
			Assert.assertNotNull( space.getDescription().getPlain() );

			System.out.println( space.getName() );
			System.out.println( space.getDescription().getPlain().getValue() );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testUpdateSpace()
	{
		Space space = new Space();
		space.setKey( "TEST" );
		space.setName( "Once more updated name" );
		space.setDescription( space.new Description() );
		space.getDescription().setPlain( space.getDescription().new Value() );
		space.getDescription().getPlain().setValue( "Updated Description plain value of space." );

		try
		{
			confluenceManager.updateSpace( space );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testDeleteSpace()
	{
		Space space = new Space();
		space.setKey( "TEST" );

		try
		{
			confluenceManager.deleteSpace( space );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testListPages()
	{
		try
		{
			Set<Page> pages = confluenceManager.listPages( "TEST", 0, 200 );

			Assert.assertNotNull( pages );
			for ( Page page : pages )
			{
				System.out.println( page.getId() + "\t" + page.getTitle() );
			}
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testCreatePage()
	{
		Page page = new Page();

		page.setTitle( "Yet another page" );
		page.setStatus( "current" );

		page.setSpace( new Space() );
		page.getSpace().setKey( "TEST" );

		page.setBodyStorageValue( "<p>This is test page's storage, created by junit test case.</p>" );

		page.setVersionNumber( 1 );

		try
		{
			Page newPage = confluenceManager.createNewPage( page );

			Assert.assertNotNull( newPage );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}

	}
	
	@Test
	public void testCreateSubPage()
	{
		Page page = new Page();

		page.setTitle( "Child page" );
		page.setStatus( "current" );

		page.setSpace( new Space() );
		page.getSpace().setKey( "TEST" );
		
		page.setAncestors( new HashSet<Page>() );
		Page parent = new Page();
		parent.setId( "1933376" );
		page.getAncestors().add( parent );

		page.setBodyStorageValue( "<p>This is test page's storage, created by junit test case.</p>" );

		page.setVersionNumber( 1 );

		try
		{
			Page newPage = confluenceManager.createNewPage( page );

			Assert.assertNotNull( newPage );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}

	}


	@Test
	public void testGetPage() throws ConfluenceManagerException
	{
		Page page = confluenceManager.getPage( "1933376" );

		Assert.assertNotNull( page );
		Assert.assertNotNull( page.getBody() );
		Assert.assertNotNull( page.getBody().getView() );
		Assert.assertNotNull( page.getBody().getStorage() );

		System.out.println( page.getBody().getView().getValue() );
		System.out.println( page.getBody().getStorage().getValue() );
	}


	@Test
	public void testUpdatePage()
	{
		Page page = new Page();
		page.setId( "1933376" );

		page.setTitle( "Yet another page with update" );
		page.setVersionNumber( 1 );

		try
		{
			confluenceManager.updatePage( page );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testDeletePage()
	{
		Page page = new Page();
		page.setId( "1933374" );

		try
		{
			confluenceManager.deletePage( page );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testGetPageLabels()
	{
		try
		{
			LabelResult result = confluenceManager.getPageLabels( "1933376" );
			Assert.assertNotNull( result );
			Assert.assertNotNull( result.getResults() );

			for ( LabelResult.Label label : result.getResults() )
			{
				System.out.println( label.getName() );
			}
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testDeletePageLabels()
	{
		try
		{
			confluenceManager.deletePageLabels( "21364969", "label2" );
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}


	@Test
	public void testAddPageLabels()
	{
		Set<String> labels = new HashSet<String>();

		labels.add( "tomcat" );
		labels.add( "jetty" );

		try
		{
			LabelResult labelResult = confluenceManager.addPageLabels( "1933376", labels );

			Assert.assertNotNull( labelResult );
			Assert.assertNotNull( labelResult.getResults() );

			for ( LabelResult.Label lbl : labelResult.getResults() )
			{
				System.out.println( lbl.getName() );
			}
		}
		catch ( ConfluenceManagerException e )
		{
			Assert.fail( e.getMessage() );
		}
	}
}
