package org.safehaus.webapp.action;


import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.JarvisContext;
import org.safehaus.stash.client.Page;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.model.Project;
import org.safehaus.util.JarvisContextHolder;


public class StashAction extends BaseAction
{
    private StashManager stashManager;

    private Page<Project> page;


    public void setStashManager( final StashManager stashManager )
    {
        this.stashManager = stashManager;
    }


    public Page<Project> getPage()
    {
        return page;
    }


    public String getBaseUrl()
    {
        return stashManager.getBaseUrl();
    }


    public String list() throws JiraClientException
    {
        log.debug( String.format( "StashManager: %s", stashManager ) );
        try
        {
            JarvisContextHolder.setContext( new JarvisContext( getSecurityCookie() ) );
            page = stashManager.getProjects( 100, 0 );
            Project p = new Project();
        }
        catch ( Exception e )
        {
            log.error( e.toString(), e );
        }
        finally
        {
            JarvisContextHolder.getContext().destroy();
        }
        return SUCCESS;
    }
}
