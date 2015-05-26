package org.safehaus.service.impl;


import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.core.UriInfo;

import org.safehaus.jira.api.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisProject;
import org.safehaus.model.Views;
import org.safehaus.service.JiraManager;
import org.safehaus.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonView;


@Service( "projectManager" )
@WebService( serviceName = "ProjectService", endpointInterface = "org.safehaus.service.ProjectService" )
public class ProjectServiceImpl implements ProjectService
{
    private static Logger logger = LoggerFactory.getLogger( ProjectServiceImpl.class );


    private JiraManager jiraManager;


    @Autowired
    public void setJiraManager( final JiraManager jiraManager )
    {
        this.jiraManager = jiraManager;
    }


    //    @Autowired
    //    public void setProjectDao( final ProjectDao projectDao )
    //    {
    //        this.dao = projectDao;
    //        this.projectDao = projectDao;
    //    }


    @Override
    public JarvisProject getProject( final String projectId ) throws JiraClientException
    {
        JarvisProject result = jiraManager.getProject( projectId );
        result.setTeamMembers( jiraManager.getProjectMemebers( projectId ) );
        return result;
    }


    @Override
    public List<JarvisProject> getProjects()
    {
        return jiraManager.getProjects();
    }


    @JsonView( Views.JarvisIssueShort.class )
    @Override
    public List<JarvisIssue> getIssues( final String projectId )
    {
        return jiraManager.getIssues( projectId );
    }


    @JsonView( Views.JarvisIssueLong.class )
    @Override
    public JarvisIssue getIssue( final String issueId )
    {
        return jiraManager.getIssue( issueId );
    }


    @Override
    public JarvisIssue createIssue( final JarvisIssue issue )
    {
        String token = issue.getToken();
        logger.debug( String.format( "Issue: %s. Token: %s", issue, token ) );
        return jiraManager.createIssue( issue, token );
    }
}
