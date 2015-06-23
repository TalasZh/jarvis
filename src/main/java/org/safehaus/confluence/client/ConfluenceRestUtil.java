package org.safehaus.confluence.client;


import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Cookie;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.safehaus.confluence.model.ConfluenceEntity;
import org.safehaus.confluence.model.LabelResult;
import org.safehaus.stash.util.AtlassianRestUtil;


public class ConfluenceRestUtil extends AtlassianRestUtil
{

	public ConfluenceRestUtil(Cookie authCookie)
	{
		super( authCookie );
	}


	public ConfluenceRestUtil(String username, String password)
	{
		super( username, password );
	}


	public String post( String url, ConfluenceEntity entity ) throws RestException
	{
		try
		{
			ClientResource clientResource = getClientResource( url );

			return clientResource.post( entity, MediaType.APPLICATION_JSON ).getText();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			throw new RestException( String.format( "Error POST'ing resource %s", url ), e );
		}

	}


	public String post( String url, Set<LabelResult.Label> labels ) throws RestException
	{
		try
		{
			ClientResource clientResource = getClientResource( url );

			return clientResource.post( labels, MediaType.APPLICATION_JSON ).getText();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			throw new RestException( String.format( "Error POST'ing resource %s", url ), e );
		}

	}


	public String put( String url, ConfluenceEntity entity ) throws RestException
	{
		try
		{
			ClientResource clientResource = getClientResource( url );

			return clientResource.put( entity, MediaType.APPLICATION_JSON ).getText();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			throw new RestException( String.format( "Error PUT'ing resource %s", url ), e );
		}

	}


	public String delete( String url, Map<String, String> queryParams ) throws RestException
	{
		try
		{
			ClientResource clientResource = getClientResource( url );

			if ( queryParams != null )
			{
				for ( Map.Entry<String, String> queryParam : queryParams.entrySet() )
				{
					clientResource.addQueryParameter( queryParam.getKey(), queryParam.getValue() );
				}
			}

			return clientResource.delete( MediaType.APPLICATION_JSON ).getText();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			throw new RestException( String.format( "Error DELETE'ing resource %s", url ), e );
		}

	}

}
