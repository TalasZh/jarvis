package org.safehaus.webapp.action;


import org.safehaus.exceptions.JiraClientException;
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


    public String list() throws JiraClientException
    {
        try
        {
            page = stashManager.getProjects( 100, 0 );
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
