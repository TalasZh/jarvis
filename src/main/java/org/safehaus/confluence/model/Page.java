package org.safehaus.confluence.model;


import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


public class Page implements ConfluenceEntity
{
	public String type = "page";

	private String id;
	private String status;
	private String title;

	private Space space;

	private Body body;

	private Version version;

	private Links _links;

	private Set<Page> ancestors;


	public String getId()
	{
		return id;
	}


	public void setId( String id )
	{
		this.id = id;
	}


	public String getStatus()
	{
		return status;
	}


	public void setStatus( String status )
	{
		this.status = status;
	}


	public String getTitle()
	{
		return title;
	}


	public void setTitle( String title )
	{
		this.title = title;
	}


	public Space getSpace()
	{
		return space;
	}


	public void setSpace( Space space )
	{
		this.space = space;
	}


	public Body getBody()
	{
		return body;
	}


	public void setBody( Body body )
	{
		this.body = body;
	}


	public Version getVersion()
	{
		return version;
	}


	public void setVersion( Version version )
	{
		this.version = version;
	}


	public Links get_links()
	{
		return _links;
	}


	public void set_links( Links _links )
	{
		this._links = _links;
	}


	public Set<Page> getAncestors()
	{
		return ancestors;
	}


	public void setAncestors( Set<Page> ancestors )
	{
		this.ancestors = ancestors;
	}


	@Override
	public String toString()
	{
		ToStringBuilder tsb = new ToStringBuilder( null );
		return tsb.append( "id", getId() ).append( "title", getTitle() ).append( "status", getStatus() ).toString();
	}


	public void setBodyValue( String storageValue )
	{
		if ( body == null )
		{
			body = new Body();
		}
		if ( body.getStorage() == null )
		{
			body.setStorage( body.new Value() );
			body.getStorage().setRepresentation( "storage" );
		}
		body.getStorage().setValue( storageValue );
	}


	public String getBodyValue()
	{
		if ( body == null || body.getStorage() == null )
		{
			return null;
		}
		else
		{
			return body.getStorage().getValue();
		}
	}


	public void setVersionNumber( int versionNumber )
	{
		if ( this.version == null )
		{
			this.version = new Version();
		}
		this.version.setNumber( versionNumber );
	}


	public int getVersionNumber()
	{
		if ( this.version == null )
		{
			return 0;
		}
		return this.version.number;
	}


	public String getWebLink()
	{
		if ( _links == null )
		{
			return null;
		}
		else
		{
			return _links.getWebui();
		}
	}

	public class Body
	{
		private Value view;
		private Value storage;


		public Value getView()
		{
			return view;
		}


		public void setView( Value view )
		{
			this.view = view;
		}


		public Value getStorage()
		{
			return storage;
		}


		public void setStorage( Value storage )
		{
			this.storage = storage;
		}

		public class Value
		{
			private String value;
			private String representation;


			public String getValue()
			{
				return value;
			}


			public void setValue( String value )
			{
				this.value = value;
			}


			public String getRepresentation()
			{
				return representation;
			}


			public void setRepresentation( String representation )
			{
				this.representation = representation;
			}

		}
	}

	public class Version
	{
		private int number;


		public int getNumber()
		{
			return number;
		}


		public void setNumber( int number )
		{
			this.number = number;
		}

	}

	public class Links
	{
		private String webui;
		private String tinyui;
		private String collection;
		private String base;


		public String getWebui()
		{
			return webui;
		}


		public void setWebui( String webui )
		{
			this.webui = webui;
		}


		public String getTinyui()
		{
			return tinyui;
		}


		public void setTinyui( String tinyui )
		{
			this.tinyui = tinyui;
		}


		public String getCollection()
		{
			return collection;
		}


		public void setCollection( String collection )
		{
			this.collection = collection;
		}


		public String getBase()
		{
			return base;
		}


		public void setBase( String base )
		{
			this.base = base;
		}

	}

}
