package org.safehaus.webapp.action;


import java.util.ArrayList;
import java.util.List;
import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.client.ConfluenceManagerException;
import org.safehaus.confluence.model.Space;
import org.safehaus.model.JarvisContext;
import org.safehaus.util.JarvisContextHolder;
import org.springframework.beans.factory.annotation.Autowired;


public class ConfluenceAction extends BaseAction
{
	private static final long serialVersionUID = -313987370694845468L;

	@Autowired
	private ConfluenceManager confluenceManager;

	private List<Space> spaces;


	public String getBaseUrl()
	{
		return confluenceManager.getBaseUrl();
	}


	public List<Space> getSpaces()
	{
		return spaces;
	}


	public String list()
	{
		try
		{
			JarvisContextHolder.setContext( new JarvisContext( getSecurityCookie() ) );
			spaces = confluenceManager.getAllSpaces();
		}
		catch ( ConfluenceManagerException e )
		{
			spaces = new ArrayList<Space>();
		}
		finally
		{
			JarvisContextHolder.getContext().destroy();
		}

		return SUCCESS;
	}

}
