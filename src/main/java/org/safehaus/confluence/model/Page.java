package org.safehaus.confluence.model;


import java.util.HashSet;
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

	private Metadata metadata;

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


	public void setBodyViewValue( String viewValue )
	{
		if ( body == null )
		{
			body = new Body();
		}
		if ( body.getView() == null )
		{
			body.setView( body.new Value() );
			body.getView().setRepresentation( "view" );
		}
		body.getView().setValue( viewValue );
	}


	public void setBodyStorageValue( String storageValue )
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


	public void addLabelString( String labelName )
	{
		if ( metadata == null )
		{
			metadata = new Metadata();
		}
		if ( metadata.getLabels() == null )
		{
			metadata.setLabels( new LabelResult() );
		}
		if ( metadata.getLabels().getResults() == null )
		{
			metadata.getLabels().setResults( new HashSet<LabelResult.Label>() );
		}
		LabelResult.Label label = metadata.getLabels().new Label();
		label.setName( labelName );
		metadata.getLabels().getResults().add( label );
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

	public class Metadata
	{
		private LabelResult labels;


		public LabelResult getLabels()
		{
			return labels;
		}


		public void setLabels( LabelResult labels )
		{
			this.labels = labels;
		}

	}

}
