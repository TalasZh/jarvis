package org.safehaus.jira.impl;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.joda.time.DateTime;
import org.safehaus.jira.api.GroupRestClient;
import org.safehaus.jira.api.JarvisJiraRestClient;
import org.safehaus.jira.api.JiraClient;
import org.safehaus.jira.api.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisIssueType;
import org.safehaus.model.JarvisMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.httpclient.api.HttpStatus;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.BasicProjectRole;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.ProjectRole;
import com.atlassian.jira.rest.client.api.domain.RoleActor;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.json.gen.IssueInputJsonGenerator;
import com.atlassian.util.concurrent.Promise;
import com.google.common.collect.ImmutableList;

import static java.util.Arrays.asList;


/**
 * Created by tzhamakeev on 5/5/15.
 */
public class JiraClientImpl implements JiraClient
{
    private static Logger logger = LoggerFactory.getLogger( JiraClientImpl.class );

    private static final String JIRA_URL = "http://test-jira.critical-factor.com";

    private JarvisJiraRestClient restClient;

    private List<Project> projects = new ArrayList<Project>();
    //
    //    ObjectMapper mapper;
    //
    //
    //    public void setMapper( final ObjectMapper mapper )
    //    {
    //        this.mapper = mapper;
    //    }


    public JiraClientImpl( String uri, final String username, final String password )
            throws JiraClientException, URISyntaxException
    {
        System.out.println( String.format( "Logging in to %s with username '%s'", uri, username ) );
        JarvisJiraRestClientFactory factory = new AsynchronousJarvisJiraRestClientFactory();

        restClient = factory.createWithBasicHttpAuthentication( new URI( uri ), username, password );
        try
        {
            // Invoke the JRJC Client
            Promise<User> promise = restClient.getUserClient().getUser( username );
            User user = promise.claim();
            // Print the result
            System.out.println( String.format( "Your admin user's email address is: %s\r\n", user.getEmailAddress() ) );
        }

        catch ( Exception e )
        {
            System.out.println( String.format( "Error on creating JiraClient: %s", e.toString() ) );
            throw new JiraClientException( e.toString(), e );
        }
    }


    public List<Project> getAllProjects()
    {
        if ( projects.size() == 0 )
        {
            Iterable<BasicProject> pList = restClient.getProjectClient().getAllProjects().claim();
            Iterator<BasicProject> i = pList.iterator();

            while ( i.hasNext() )
            {
                BasicProject p = i.next();

                projects.add( restClient.getProjectClient().getProject( p.getSelf() ).claim() );
            }
        }

        return projects;
    }


    public Project getProject( String projectId ) throws JiraClientException
    {
        Iterator<Project> i = projects.iterator();
        Project result = null;
        while ( i.hasNext() )
        {
            Project p = i.next();
            if ( p.getKey().equals( projectId ) )
            {
                result = p;
                break;
            }
        }

        if ( result == null )
        {
            try
            {
                result = restClient.getProjectClient().getProject( projectId ).claim();
            }
            catch ( RestClientException e )
            {
                if ( e.getStatusCode().get() == HttpStatus.NOT_FOUND.code )
                {
                    throw new JiraClientException(
                            String.format( "No project could be found with key '%s'.", projectId ), e );
                }
            }
        }
        return result;
    }


    public List<Issue> getIssues( final String projectId, final String componentId, final int maxResult,
                                  final int startIndex, final String issueType ) throws JiraClientException
    {
        Project project = getProject( projectId );
        Iterator<BasicComponent> componentIterator = project.getComponents().iterator();

        List<Issue> result = new ArrayList<Issue>();

        final SearchResult searchResult = restClient.getSearchClient().searchJql(
                String.format( "project = %s and component = %s and issueType = %s order by duedate", projectId,
                        componentId, issueType ), maxResult, startIndex, null ).claim();

        for ( Issue issue : searchResult.getIssues() )
        {
            result.add( issue );
        }
        return result;
    }


    @Override
    public List<Issue> getIssues( final String projectId, final int maxResult, final int startIndex,
                                  final String issueType ) throws JiraClientException
    {
        Project project = getProject( projectId );
        Iterator<BasicComponent> componentIterator = project.getComponents().iterator();

        List<Issue> result = new ArrayList<Issue>();

        final SearchResult searchResult = restClient.getSearchClient().searchJql(
                String.format( "project = %s and issueType = %s order by duedate", projectId, issueType ), maxResult,
                startIndex, null ).claim();

        for ( Issue issue : searchResult.getIssues() )
        {
            result.add( issue );
        }
        return result;
    }


    @Override
    public List<JarvisIssue> getIssues( final String projectId )
    {
        List<JarvisIssue> result = new ArrayList<JarvisIssue>();
        final SearchResult searchResult =
                restClient.getSearchClient().searchJql( String.format( "project = %s order by duedate", projectId ) )
                          .claim();

        for ( Issue issue : searchResult.getIssues() )
        {
            JarvisIssue jarvisIssue = new JarvisIssue( issue.getId(), issue.getKey(), issue.getSummary(), projectId,
                    new JarvisIssueType( issue.getIssueType().getId(), issue.getIssueType().getName() ) );
            result.add( jarvisIssue );
        }
        return result;
    }


    public List<Component> getAllComponents( String projectId ) throws JiraClientException
    {
        Project project = getProject( projectId );

        List<Component> result = new ArrayList<Component>();
        Iterator<BasicComponent> componentIterator = project.getComponents().iterator();

        while ( componentIterator.hasNext() )
        {
            BasicComponent basicComponent = componentIterator.next();
            result.add( restClient.getComponentClient().getComponent( basicComponent.getSelf() ).claim() );
        }
        return result;
    }


    @Override
    public Issue getIssue( String issueKey )
    {
        return restClient.getIssueClient().getIssue( issueKey ).claim();
    }


    @Override
    public Issue createIssue( JarvisIssue jarvisIssue, String token )
    {
        logger.debug( String.format( "=================> %s %s", jarvisIssue, token ) );
        JarvisJiraRestClientFactory factory = new AsynchronousJarvisJiraRestClientFactory();

        JarvisJiraRestClient client = null;
        try
        {
            client = factory.create( new URI( "http://test-jira.critical-factor.com" ),
                    new CrowdAuthenticationHandler( token ) );
            logger.debug( client.getMetadataClient().getServerInfo().claim().toString() );
        }
        catch ( Exception e )
        {
            logger.debug( e.getMessage() );
            e.printStackTrace();
        }

        //        User assignee = restClient.getUserClient().getUser( jarvisIssue.getAssignee() ).claim();
        IssueInputBuilder issueInputBuilder =
                new IssueInputBuilder( jarvisIssue.getProjectKey(), jarvisIssue.getType().getId(),
                        jarvisIssue.getSummary() );

        issueInputBuilder.setDescription( jarvisIssue.getIssueDescription() );
        issueInputBuilder.setReporterName( jarvisIssue.getReporter() );
        issueInputBuilder.setDueDate( new DateTime().plusDays( 5 ) );
        issueInputBuilder.setAssigneeName( jarvisIssue.getAssignee() );

        //        issueInputBuilder.setFieldValue( "assignee", assignee );
        IssueInput issueInput = issueInputBuilder.build();
        BasicIssue createdIssue = client.getIssueClient().createIssue( issueInput ).claim();
        logger.debug( "Created issue: " + createdIssue );

        Issue i = client.getIssueClient().getIssue( createdIssue.getKey() ).claim();
//        issueInputBuilder = new IssueInputBuilder( /*jarvisIssue.getProjectKey(), jarvisIssue.getType().getId() */ );
//        issueInputBuilder.setAssigneeName( jarvisIssue.getAssignee() );

        //        issueInputBuilder.set setFieldValue( "assignee",  new FieldInput( "name", jarvisIssue.getAssignee()
        // ) );
//        issueInput = issueInputBuilder.build();
//
//        IssueInputJsonGenerator gen = new IssueInputJsonGenerator();
//        try
//        {
//            logger.debug( gen.generate( issueInput ).toString() );
//        }
//        catch ( JSONException e )
//        {
//            e.printStackTrace();
//        }
//        client.getIssueClient().updateIssue( i.getKey(), issueInput ).claim();
        return i;
    }


    @Override
    public List<JarvisMember> getProjectMemebers( final String projectId ) throws JiraClientException
    {
        List<JarvisMember> result = new ArrayList<>();

        Project project = getProject( projectId );

        for ( Iterator<BasicProjectRole> rolesIterator = project.getProjectRoles().iterator();
              rolesIterator.hasNext(); )
        {

            BasicProjectRole role = rolesIterator.next();
            ProjectRole projectRole = restClient.getProjectRolesRestClient().getRole( role.getSelf() ).claim();
            for ( Iterator<RoleActor> actorIterator = projectRole.getActors().iterator(); actorIterator.hasNext(); )
            {
                RoleActor actor = actorIterator.next();

                Group group = restClient.getGroupClient().getGroup( actor.getName(),
                        ImmutableList.copyOf( asList( GroupRestClient.Expandos.values() ) ) ).claim();

                for ( User user : group.getUsers() )
                {
                    result.add(
                            new JarvisMember( user.getName(), user.getAvatarUri().toString(), user.getDisplayName() ) );
                }
            }
        }
        return result;
    }


    public void close() throws IOException
    {
        restClient.close();
    }
}
