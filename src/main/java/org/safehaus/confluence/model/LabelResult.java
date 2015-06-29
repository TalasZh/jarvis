package org.safehaus.confluence.model;


import java.io.Serializable;
import java.util.Set;


public class LabelResult implements ConfluenceEntity
{

	private Set<Label> results;


	public Set<Label> getResults()
	{
		return results;
	}


	public void setResults( Set<Label> results )
	{
		this.results = results;
	}

	public class Label implements Serializable
	{
		private static final long serialVersionUID = 272692206883480535L;

		private String prefix;
		private String name;
		private Long id;


		public String getPrefix()
		{
			return prefix;
		}


		public void setPrefix( String prefix )
		{
			this.prefix = prefix;
		}


		public String getName()
		{
			return name;
		}


		public void setName( String name )
		{
			this.name = name;
		}


		public Long getId()
		{
			return id;
		}


		public void setId( Long id )
		{
			this.id = id;
		}

	}

}
