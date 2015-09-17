package org.safehaus.webapp.action;


import java.util.List;

import javax.ws.rs.core.Cookie;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraManager;
import org.safehaus.jira.model.JarvisIssue;
import org.safehaus.model.JarvisContext;
import org.safehaus.model.JarvisProject;
import org.safehaus.util.JarvisContextHolder;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.Preparable;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class JiraAction extends BaseAction implements Preparable
{
    //    private static Logger logger = LoggerFactory.getLogger( JiraAction.class );
    private JiraManager jiraManager;

    private List<JarvisProject> projects = Lists.newArrayList();
    private String jiraUrl;
    private String securityCookieName;
    private String project;
    private List<JarvisIssue> issues = Lists.newArrayList();


    public void setJiraManager( final JiraManager jiraManager )
    {
        this.jiraManager = jiraManager;
    }


    public void setSecurityCookieName( final String securityCookieName )
    {
        this.securityCookieName = securityCookieName;
    }


    public String getJiraUrl()
    {
        return jiraUrl;
    }


    public void setJiraUrl( final String jiraUrl )
    {
        this.jiraUrl = jiraUrl;
    }


    public List<JarvisProject> getProjects()
    {
        return projects;
    }


    public String getProject()
    {
        return project;
    }


    public void setProject( final String project )
    {
        this.project = project;
    }


    public List<JarvisIssue> getIssues()
    {
        return issues;
    }


    public String list() throws JiraClientException
    {
        try
        {
            JarvisContextHolder.setContext( new JarvisContext( jiraUrl, getSecurityCookie() ) );

            projects = jiraManager.getProjects();

            if ( project == null )
            {
                return SUCCESS;
            }

            issues = jiraManager.getIssues( project );
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


    protected Cookie getSecurityCookie()
    {
        Cookie result = null;
        for ( javax.servlet.http.Cookie cookie : getRequest().getCookies() )
        {
            if ( cookie.getName().equals( securityCookieName ) )
            {
                result = new Cookie( securityCookieName, cookie.getValue() );
                break;
            }
        }

        return result;
    }


    @Override
    public void prepare() throws Exception
    {

    }
}
