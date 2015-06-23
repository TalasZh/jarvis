package org.safehaus.confluence.client;


public class ConfluenceManagerException extends Exception
{
	private static final long serialVersionUID = -8564308687754429945L;


	public ConfluenceManagerException(Throwable e)
	{
		super( e );
	}


	public ConfluenceManagerException(String cause)
	{
		super( cause );
	}
}
