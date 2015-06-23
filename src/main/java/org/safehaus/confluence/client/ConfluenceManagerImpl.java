package org.safehaus.confluence.client;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Cookie;
import org.safehaus.confluence.model.ConfluenceEntity;
import org.safehaus.confluence.model.LabelResult;
import org.safehaus.confluence.model.Page;
import org.safehaus.confluence.model.PageResult;
import org.safehaus.confluence.model.Space;
import org.safehaus.stash.util.AtlassianRestUtil.RestException;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;


public class ConfluenceManagerImpl implements ConfluenceManager
{
	private String baseUrl;

	private String username;
	private String password;

	private Cookie cookie;


	public ConfluenceManagerImpl(String baseUrl)
	{
		Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );

		this.baseUrl = baseUrl;
	}


	public ConfluenceManagerImpl(String baseUrl, String username, String password)
	{
		Preconditions.checkArgument( !Strings.isNullOrEmpty( baseUrl ) );
		Preconditions.checkArgument( !Strings.isNullOrEmpty( username ) );
		Preconditions.checkArgument( !Strings.isNullOrEmpty( username ) );

		this.baseUrl = baseUrl;
		this.username = username;
		this.password = password;
	}


	public ConfluenceManagerImpl(String baseUrl, Cookie cookie)
	{
		this.baseUrl = baseUrl;
		this.cookie = cookie;
	}


	protected String get( String apiPath, Map<String, String> parameters ) throws RestException,
	        ConfluenceManagerException
	{
		if ( !Strings.isNullOrEmpty( username ) && !Strings.isNullOrEmpty( password ) )
		{
			return new ConfluenceRestUtil( username, password ).get( String.format( "%s/%s", baseUrl, apiPath ),
			        parameters );
		}
		else if ( cookie != null )
		{
			return new ConfluenceRestUtil( cookie ).get( String.format( "%s/%s", baseUrl, apiPath ), parameters );
		}
		else
		{
			throw new ConfluenceManagerException( "No valid authorization info found" );
		}
	}


	protected String post( String apiPath, ConfluenceEntity entity ) throws RestException, ConfluenceManagerException
	{
		if ( !Strings.isNullOrEmpty( username ) && !Strings.isNullOrEmpty( password ) )
		{
			return new ConfluenceRestUtil( username, password ).post( String.format( "%s/%s", baseUrl, apiPath ),
			        entity );
		}
		else if ( cookie != null )
		{
			return new ConfluenceRestUtil( cookie ).post( String.format( "%s/%s", baseUrl, apiPath ), entity );
		}
		else
		{
			throw new ConfluenceManagerException( "No valid authorization info found." );
		}
	}


	protected String post( String apiPath, Set<LabelResult.Label> labels ) throws RestException,
	        ConfluenceManagerException
	{
		if ( !Strings.isNullOrEmpty( username ) && !Strings.isNullOrEmpty( password ) )
		{
			return new ConfluenceRestUtil( username, password ).post( String.format( "%s/%s", baseUrl, apiPath ),
			        labels );
		}
		else if ( cookie != null )
		{
			return new ConfluenceRestUtil( cookie ).post( String.format( "%s/%s", baseUrl, apiPath ), labels );
		}
		else
		{
			throw new ConfluenceManagerException( "No valid authorization info found." );
		}
	}


	protected String put( String apiPath, ConfluenceEntity entity ) throws RestException, ConfluenceManagerException
	{
		if ( !Strings.isNullOrEmpty( username ) && !Strings.isNullOrEmpty( password ) )
		{
			return new ConfluenceRestUtil( username, password )
			        .put( String.format( "%s/%s", baseUrl, apiPath ), entity );
		}
		else if ( cookie != null )
		{
			return new ConfluenceRestUtil( cookie ).put( String.format( "%s/%s", baseUrl, apiPath ), entity );
		}
		else
		{
			throw new ConfluenceManagerException( "No valid authorization info found" );
		}
	}


	protected String delete( String apiPath, Map<String, String> parameters ) throws RestException,
	        ConfluenceManagerException
	{
		if ( !Strings.isNullOrEmpty( username ) && !Strings.isNullOrEmpty( password ) )
		{
			return new ConfluenceRestUtil( username, password ).delete( String.format( "%s/%s", baseUrl, apiPath ),
			        parameters );
		}
		else if ( cookie != null )
		{
			return new ConfluenceRestUtil( cookie ).delete( String.format( "%s/%s", baseUrl, apiPath ), parameters );
		}
		else
		{
			throw new ConfluenceManagerException( "No valid authorization info found" );
		}
	}


	protected String delete( String apiPath ) throws RestException, ConfluenceManagerException
	{
		return delete( apiPath, null );
	}


	@Override
	public List<Page> findPages( Page page ) throws ConfluenceManagerException
	{
		Preconditions.checkNotNull( page );
		Preconditions.checkNotNull( page.getSpace() );
		Preconditions.checkNotNull( page.getSpace().getKey() );

		Preconditions.checkNotNull( page.getTitle() );

		try
		{
			Map<String, String> params = new HashMap<String, String>();
			params.put( "spaceKey", page.getSpace().getKey() );
			params.put( "title", page.getTitle() );

			String response = get( "rest/api/content", params );

			PageResult pageResult = new Gson().fromJson( response, PageResult.class );

			List<Page> pages = new ArrayList<Page>();
			pages.addAll( pageResult.getResults() );

			return pages;
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public Set<Page> listPages( String spaceKey, Integer start, Integer limit ) throws ConfluenceManagerException
	{
		try
		{
			Map<String, String> params = new HashMap<String, String>();
			if ( spaceKey != null )
			{
				params.put( "spaceKey", spaceKey );
			}
			if ( start != null )
			{
				params.put( "start", String.valueOf( start ) );
			}
			if ( limit != null )
			{
				params.put( "limit", String.valueOf( limit ) );
			}
			String response = get( "rest/api/content", params );

			PageResult pageResult = new Gson().fromJson( response, PageResult.class );

			return pageResult.getResults();
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public Page getPage( String pageId ) throws ConfluenceManagerException
	{
		try
		{
			Map<String, String> params = new HashMap<String, String>();
			params.put( "expand", "body.view,body.storage,space,version" );
			String response = get( String.format( "rest/api/content/%s", pageId ), params );

			Page page = new Gson().fromJson( response, Page.class );

			return page;
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public Page createNewPage( Page page ) throws ConfluenceManagerException
	{
		try
		{
			String response = post( "rest/api/content", page );

			Page aPage = new Gson().fromJson( response, Page.class );

			return aPage;
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public void updatePage( Page page ) throws ConfluenceManagerException
	{
		Preconditions.checkNotNull( page );
		Preconditions.checkNotNull( page.getTitle() );

		page.setVersionNumber( page.getVersionNumber() + 1 );

		try
		{
			String response = put( String.format( "rest/api/content/%s", page.getId() ), page );

			System.out.println( response );
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public void deletePage( Page page ) throws ConfluenceManagerException
	{
		Preconditions.checkNotNull( page );
		Preconditions.checkNotNull( page.getId() );

		try
		{
			delete( String.format( "rest/api/content/%s", page.getId() ) );
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public Space createNewSpace( Space space ) throws ConfluenceManagerException
	{
		Preconditions.checkNotNull( space );
		Preconditions.checkNotNull( space.getKey() );
		Preconditions.checkNotNull( space.getName() );
		Preconditions.checkNotNull( space.getDescription() );

		try
		{
			String response = post( "rest/api/space", space );

			Space aSpace = new Gson().fromJson( response, Space.class );

			return aSpace;
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public void updateSpace( Space space ) throws ConfluenceManagerException
	{
		Preconditions.checkNotNull( space );
		Preconditions.checkNotNull( space.getKey() );
		Preconditions.checkNotNull( space.getName() );

		try
		{
			put( String.format( "rest/api/space/%s", space.getKey() ), space );
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
		catch ( Exception e )
		{
			throw new ConfluenceManagerException( e );
		}
	}


	@Override
	public Space getSpace( String key ) throws ConfluenceManagerException
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put( "expand", "description.plain" );

		try
		{
			String response = get( String.format( "rest/api/space/%s", key ), parameters );

			Space space = new Gson().fromJson( response, Space.class );

			return space;
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
	}


	@Override
	public void deleteSpace( Space space ) throws ConfluenceManagerException
	{
		try
		{
			delete( String.format( "rest/api/space/%s", space.getKey() ) );
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
	}


	@Override
	public LabelResult getPageLabels( String pageId ) throws ConfluenceManagerException
	{
		try
		{
			String response = get( String.format( "rest/api/content/%s/label", pageId ), null );

			LabelResult result = new Gson().fromJson( response, LabelResult.class );

			return result;
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getCause() );
		}
	}


	@Override
	public LabelResult addPageLabels( String pageId, Set<String> labelNames ) throws ConfluenceManagerException
	{
		try
		{
			LabelResult res = new LabelResult();
			res.setResults( new HashSet<LabelResult.Label>() );

			for ( String labelName : labelNames )
			{
				LabelResult.Label label = res.new Label();
				label.setPrefix( "global" );
				label.setName( labelName );
				res.getResults().add( label );
			}

			String response = post( String.format( "rest/api/content/%s/label", pageId ), res.getResults() );

			LabelResult result = new Gson().fromJson( response, LabelResult.class );

			return result;
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
	}


	@Override
	public void deletePageLabels( String pageId, String labelName ) throws ConfluenceManagerException
	{
		try
		{
			delete( String.format( "rest/api/content/%s/label/%s", pageId, labelName ) );
		}
		catch ( RestException e )
		{
			throw new ConfluenceManagerException( e.getMessage() );
		}
	}
}
