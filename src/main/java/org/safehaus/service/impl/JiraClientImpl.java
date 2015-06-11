package org.safehaus.service.impl;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.joda.time.DateTime;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisIssueType;
import org.safehaus.model.JarvisMember;
import org.safehaus.service.JiraClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.httpclient.api.HttpStatus;
import com.atlassian.jira.rest.client.api.AuthenticationHandler;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicComponent;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.User;
import com.atlassian.jira.rest.client.api.domain.input.ComplexIssueInputFieldValue;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.ServerVersionConstants;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.google.common.collect.ImmutableList;


/**
 * Created by tzhamakeev on 5/5/15.
 */
public class JiraClientImpl implements JiraClient
{
    private static final String START_PROGRESS = "Start Progress";
    private static final String REOPEN_ISSUE = "Reopen Issue";
    private static final String RESOLVE_ISSUE = "Resolve Issue";
    private static Logger logger = LoggerFactory.getLogger( JiraClientImpl.class );

    private String uri;

    private JiraRestClient restClient;

    private List<Project> projects = new ArrayList<Project>();


    public JiraClientImpl( String uri, final String username, final String password )
            throws JiraClientException, URISyntaxException
    {
        logger.debug( String.format( "Logging in to %s with username '%s'", uri, username ) );
        this.uri = uri;
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

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


    public JiraClientImpl( String uri, final AuthenticationHandler authenticationHandler ) throws JiraClientException
    {
        this.uri = uri;
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        try
        {
            restClient = factory.create( new URI( uri ), authenticationHandler );
        }
        catch ( URISyntaxException e )
        {
            throw new JiraClientException( e.getMessage(), e );
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
    public Issue createIssue( JarvisIssue jarvisIssue ) throws JiraClientException
    {
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();

        //        JarvisJiraRestClient client = null;
        //        try
        //        {
        //            client = factory.create( new URI( uri ), new CrowdAuthenticationHandler( token ) );
        //        }
        //        catch ( Exception e )
        //        {
        //            logger.debug( e.getMessage() );
        //            throw new JiraClientException( e.getMessage(), e );
        //        }

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
        BasicIssue createdIssue = restClient.getIssueClient().createIssue( issueInput ).claim();

        logger.debug( "Created new issue: " + createdIssue.getKey() );
        Issue i = restClient.getIssueClient().getIssue( createdIssue.getKey() ).claim();
        //        issueInputBuilder = new IssueInputBuilder( /*jarvisIssue.getProjectKey(), jarvisIssue.getType()
        // .getId() */ );
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
        final Set<JarvisMember> result = new HashSet<>();

        SearchResult searchResult = restClient.getSearchClient().searchJql(
                String.format( "project=%s and assignee is not EMPTY", projectId ) ).claim();

        searchResult.getIssues().forEach( new Consumer<Issue>()
        {
            @Override
            public void accept( final Issue issue )
            {

                User user = issue.getAssignee();
                result.add( new JarvisMember( user.getName(), user.getAvatarUri().toString(), user.getDisplayName() ) );

                user = issue.getReporter();
                result.add( new JarvisMember( user.getName(), user.getAvatarUri().toString(), user.getDisplayName() ) );
            }
        } );

        return ImmutableList.copyOf( result );
    }


    private Transition getTransitionByName( Iterable<Transition> transitions, String transitionName )
    {
        for ( Transition transition : transitions )
        {
            if ( transition.getName().equals( transitionName ) )
            {
                return transition;
            }
        }
        return null;
    }


    public void updateIssueState( String issueKeyOrId, Integer transitionId )
    {
        Issue issue = getIssue( issueKeyOrId );

        logger.debug( "Current status: " + issue.getStatus() );
        logger.debug( String.format( "Issue key or ID=%s. transition ID:%d", issueKeyOrId, transitionId ) );


        final Transition[] lastTransition = new Transition[1];
        restClient.getIssueClient().getTransitions( issue ).claim().forEach( new Consumer<Transition>()
        {
            @Override
            public void accept( final Transition transition )
            {
                logger.debug( String.format( "Transition: %s", transition.toString() ) );
                lastTransition[0] = transition;
            }
        } );


        logger.debug( issue.getTransitionsUri().toString() );

        final int buildNumber = restClient.getMetadataClient().getServerInfo().claim().getBuildNumber();

        // now let's start progress on this issue
        final Iterable<Transition> transitions =
                restClient.getIssueClient().getTransitions( issue.getTransitionsUri() ).claim();
        final Transition startProgressTransition = getTransitionByName( transitions, "Start Progress" );

        if ( startProgressTransition == null )
        {
            return;
        }

        restClient.getIssueClient()
                  .transition( issue.getTransitionsUri(), new TransitionInput( startProgressTransition.getId() ) )
                  .claim();

        // and now let's resolve it as Incomplete
        final Transition resolveIssueTransition = getTransitionByName( transitions, "Resolve Issue" );
        final Collection<FieldInput> fieldInputs;

        // Starting from JIRA 5, fields are handled in different way -
        if ( buildNumber > ServerVersionConstants.BN_JIRA_5 )
        {
            fieldInputs = Arrays.asList(
                    new FieldInput( "resolution", ComplexIssueInputFieldValue.with( "name", "Incomplete" ) ) );
        }
        else
        {
            fieldInputs = Arrays.asList( new FieldInput( "resolution", "Incomplete" ) );
        }
        final TransitionInput transitionInput =
                new TransitionInput( resolveIssueTransition.getId(), fieldInputs, Comment.valueOf( "My comment" ) );
        restClient.getIssueClient().transition( issue.getTransitionsUri(), transitionInput ).claim();
    }


    @Override
    public void startIssue( String issueKeyOrId ) throws JiraClientException
    {
        Issue issue = getIssue( issueKeyOrId );

        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );

        Iterable<Transition> transitions = restClient.getIssueClient().getTransitions( issue ).claim();
        Transition toTransition = getTransitionByName( transitions, START_PROGRESS );
        if ( toTransition == null )
        {
            toTransition = getTransitionByName( transitions, REOPEN_ISSUE );
        }

        if ( toTransition == null )
        {
            throw new JiraClientException( "Could not start issue." );
        }

        final TransitionInput transitionInput = new TransitionInput( toTransition.getId(),
                Comment.valueOf( String.format( "Action \"%s\" issued by Jarvis.", toTransition.getName() ) ) );
        restClient.getIssueClient().transition( issue.getTransitionsUri(), transitionInput ).claim();
    }


    @Override
    public void resolveIssue( String issueKeyOrId ) throws JiraClientException
    {
        Issue issue = getIssue( issueKeyOrId );

        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );

        Iterable<Transition> transitions = restClient.getIssueClient().getTransitions( issue ).claim();
        Transition toTransition = getTransitionByName( transitions, RESOLVE_ISSUE );

        if ( toTransition == null )
        {
            throw new JiraClientException( "Could not resolve issue." );
        }

        final TransitionInput transitionInput = new TransitionInput( toTransition.getId(),
                Comment.valueOf( String.format( "Action \"%s\" issued by Jarvis.", toTransition.getName() ) ) );
        restClient.getIssueClient().transition( issue.getTransitionsUri(), transitionInput ).claim();
    }


    @Override
    public Iterable<Transition> getTransitions( String issueKeyOrId ) throws JiraClientException
    {
        Issue issue = getIssue( issueKeyOrId );

        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );

        return restClient.getIssueClient().getTransitions( issue ).claim();
    }


    public void close() throws IOException
    {
        restClient.close();
    }
}
