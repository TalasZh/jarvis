package org.safehaus.jira;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.safehaus.dao.entities.jira.JarvisIssue;
import org.safehaus.dao.entities.jira.JarvisMember;
import org.safehaus.exceptions.JiraClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Component;
import net.rcarz.jiraclient.ICredentials;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Project;
import net.rcarz.jiraclient.Status;
import net.rcarz.jiraclient.Transition;
import net.rcarz.jiraclient.User;


/**
 * Created by tzhamakeev on 5/5/15.
 */
public class JiraRestClientImpl implements JiraRestClient
{
    private static final String START_PROGRESS = "Start Progress";
    private static final String REOPEN_ISSUE = "Reopen Issue";
    private static final String RESOLVE_ISSUE = "Resolve Issue";
    private static Logger logger = LoggerFactory.getLogger( JiraRestClientImpl.class );

    private String uri;

    private JiraClient jiraClient;

    private List<Project> projects = new ArrayList<Project>();


    public JiraRestClientImpl( String uri, final String username, final String password )
            throws JiraClientException, URISyntaxException
    {
        logger.debug( String.format( "Logging in to %s with username '%s'", uri, username ) );
        this.uri = uri;
        try
        {
            jiraClient = new JiraClient( uri, new BasicCredentials( username, password ) );
        }
        catch ( JiraException e )
        {
            throw new JiraClientException( "Basic authentication error", e );
        }
    }


    public JiraRestClientImpl( String uri, final ICredentials authenticationHandler ) throws JiraClientException
    {
        this.uri = uri;
        try
        {
            jiraClient = new JiraClient( uri, authenticationHandler );
        }
        catch ( Exception e )
        {
            throw new JiraClientException( e.getMessage(), e );
        }
    }


    public List<Project> getAllProjects()
    {
        try
        {
            if ( projects.size() == 0 )
            {
                projects.addAll( jiraClient.getProjects() );
            }
        }
        catch ( Exception e )
        {
            logger.error( "Couldn't get project specific information {}", e.getMessage() );
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
                result = jiraClient.getProject( projectId );
            }
            catch ( JiraException e )
            {
                throw new JiraClientException( String.format( "No project could be found with key '%s'.", projectId ),
                        e );
            }
        }
        return result;
    }


    private List<Issue> searchJira( String jql, Integer maxResult, Integer startIndex )
    {
        List<Issue> result = Lists.newArrayList();
        if ( maxResult == null )
        {
            maxResult = 50;
        }
        if ( startIndex == null )
        {
            startIndex = 0;
        }
        try
        {
            Issue.SearchResult searchResult =
                    jiraClient.searchIssues( jql, "*all", "changelog", maxResult, startIndex );
            result.addAll( searchResult.issues );
        }
        catch ( JiraException e )
        {
            logger.error( "Couldn't get issues by jql: " + jql, e );
        }
        return result;
    }


    public List<Issue> getIssues( final String projectId, final String componentId, final int maxResult,
                                  final int startIndex, final String issueType ) throws JiraClientException
    {
        String jql = String.format( "project = %s " + "and component = %s " + "and issueType = %s order by duedate",
                projectId, componentId, issueType );
        return searchJira( jql, maxResult, startIndex );
    }


    @Override
    public List<Issue> getIssues( final String projectId, final int maxResult, final int startIndex,
                                  final String issueType ) throws JiraClientException
    {
        String jql = String.format( "project = %s and issueType = %s order by duedate", projectId, issueType );
        return searchJira( jql, maxResult, startIndex );
    }


    @Override
    public List<Issue> getIssues( final String projectId )
    {
        //        List<JarvisIssue> result = new ArrayList<JarvisIssue>();
        String jql = String.format( "project = '%s' order by duedate", projectId );
        return searchJira( jql, 60, null );
    }


    @Override
    public List<Issue> getIssues( final String projectId, final int max, final int startIndex )
    {
        String jql = String.format( "project = '%s' ORDER BY updated DESC", projectId );
        return searchJira( jql, max, startIndex );
    }


    @Override
    public Status changeStatus( final String issueIdOrKey, final String actionName ) throws JiraClientException
    {
        Issue issue = getIssue( issueIdOrKey );

        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );

        try
        {
            Iterable<Transition> transitions = issue.getTransitions();
            Transition toTransition = getTransitionByName( transitions, actionName );

            if ( toTransition != null )
            {
                Issue.FluentTransition fluentTransition = issue.transition();
                fluentTransition
                        .field( "Comment", String.format( "Action \"%s\" issued by Jarvis.", toTransition.getName() ) );
                fluentTransition.execute( toTransition.getName() );
            }
            else
            {
                throw new JiraClientException( String.format( "It is not possible to execute '%s'.", actionName ) );
            }
        }
        catch ( JiraException e )
        {
            throw new JiraClientException( "Error changing issue status", e );
        }
        return getIssue( issueIdOrKey ).getStatus();
    }


    @Override
    public Status changeStatus( final String issueIdOrKey, final int transitonId ) throws JiraClientException
    {
        Issue issue = getIssue( issueIdOrKey );

        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );

        try
        {
            Iterable<Transition> transitions = issue.getTransitions();
            Transition toTransition = getTransitionById( transitions, String.valueOf( transitonId ) );

            if ( toTransition != null )
            {
                Issue.FluentTransition fluentTransition = issue.transition();
                fluentTransition.execute( toTransition );
            }
            else
            {
                throw new JiraClientException(
                        String.format( "It is not possible to change state '%d'.", transitonId ) );
            }
        }
        catch ( JiraException e )
        {
            throw new JiraClientException( "Error changing issue transition.", e );
        }
        return getIssue( issueIdOrKey ).getStatus();
    }


    public List<Component> getAllComponents( String projectId ) throws JiraClientException
    {
        Project project = getProject( projectId );
        return project.getComponents();
    }


    @Override
    public Issue getIssue( String issueKey )
    {
        Issue result = null;
        try
        {
            result = jiraClient.getIssue( issueKey, "", "changelog" );
        }
        catch ( JiraException e )
        {
            logger.error( "Error getting issue metadata.", e );
        }
        return result;
    }


    @Override
    public Issue createIssue( JarvisIssue jarvisIssue ) throws JiraClientException
    {
        try
        {
            Issue.FluentCreate fluentCreate = jiraClient.createIssue( jarvisIssue.getProjectKey(), "Task" );
            fluentCreate.field( "assignee", jarvisIssue.getAssignee() );
            fluentCreate.field( "components", jarvisIssue.getComponents() );
            fluentCreate.field( "dateCreated", jarvisIssue.getDateCreated() );
            fluentCreate.field( "fixVersion", jarvisIssue.getFixVersion() );
            return fluentCreate.execute();
        }
        catch ( JiraException e )
        {
            throw new JiraClientException( "Error creating issue", e );
        }
    }


    @Override
    public List<JarvisMember> getProjectMembers( final String projectId ) throws JiraClientException
    {
        final Set<JarvisMember> result = new HashSet<>();

        String jql = String.format( "project=%s and assignee is not EMPTY", projectId );
        List<Issue> issues = searchJira( jql, null, null );

        issues.forEach( new Consumer<Issue>()
        {
            @Override
            public void accept( final Issue issue )
            {
                User assignee = issue.getAssignee();
                if ( assignee != null )
                {
                    result.add( new JarvisMember( assignee.getName(), assignee.getUrl(), assignee.getDisplayName() ) );
                }
                User reporter = issue.getReporter();
                if ( reporter != null )
                {
                    result.add( new JarvisMember( reporter.getName(), reporter.getUrl(), reporter.getDisplayName() ) );
                }
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


    private Transition getTransitionById( Iterable<Transition> transitions, String transitionId )
    {
        for ( Transition transition : transitions )
        {
            if ( transition.getId().equals( transitionId ) )
            {
                return transition;
            }
        }
        return null;
    }


    public void updateIssueState( String issueKey, Integer transitionId )
    {
        Issue issue = null;
        try
        {
            issue = getIssue( issueKey );
            Transition transition = getTransitionById( issue.getTransitions(), String.valueOf( transitionId ) );
            Issue.FluentTransition fluentTransition = issue.transition();
            fluentTransition.execute( transitionId );
        }
        catch ( JiraException e )
        {
            logger.error( "Error updating issue state.", e );
        }


        //        logger.debug( "Current status: " + issue.getStatus() );
        //        logger.debug( String.format( "Issue key or ID=%s. transition ID:%d", issueKey, transitionId ) );
        //
        //        final Transition[] lastTransition = new Transition[1];
        //        restClient.getIssueClient().getTransitions( issue ).claim().forEach( new Consumer<Transition>()
        //        {
        //            @Override
        //            public void accept( final Transition transition )
        //            {
        //                logger.debug( String.format( "Transition: %s", transition.toString() ) );
        //                lastTransition[0] = transition;
        //            }
        //        } );
        //
        //
        //        final int buildNumber = restClient.getMetadataClient().getServerInfo().claim().getBuildNumber();
        //
        //        // now let's start progress on this issue
        //        final Iterable<Transition> transitions =
        //                restClient.getIssueClient().getTransitions( issue.getTransitionsUri() ).claim();
        //        final Transition startProgressTransition = getTransitionByName( transitions, "Start Progress" );
        //
        //        if ( startProgressTransition == null )
        //        {
        //            return;
        //        }
        //
        //        restClient.getIssueClient()
        //                  .transition( issue.getTransitionsUri(), new TransitionInput( startProgressTransition
        // .getId() ) )
        //                  .claim();
        //
        //        // and now let's resolve it as Incomplete
        //        final Transition resolveIssueTransition = getTransitionByName( transitions, "Resolve Issue" );
        //        final Collection<FieldInput> fieldInputs;
        //
        //        // Starting from JIRA 5, fields are handled in different way -
        //        if ( buildNumber > ServerVersionConstants.BN_JIRA_5 )
        //        {
        //            fieldInputs = Arrays.asList(
        //                    new FieldInput( "resolution", ComplexIssueInputFieldValue.with( "name", "Incomplete" )
        // ) );
        //        }
        //        else
        //        {
        //            fieldInputs = Arrays.asList( new FieldInput( "resolution", "Incomplete" ) );
        //        }
        //        final TransitionInput transitionInput =
        //                new TransitionInput( resolveIssueTransition.getId(), fieldInputs, Comment.valueOf( "My
        // comment" ) );
        //        restClient.getIssueClient().transition( issue.getTransitionsUri(), transitionInput ).claim();
    }


    @Override
    public void startIssue( String issueKeyOrId ) throws JiraClientException
    {
        Issue issue = getIssue( issueKeyOrId );
        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );
        Iterable<Transition> transitions = null;
        try
        {
            transitions = issue.getTransitions();
        }
        catch ( JiraException e )
        {
            logger.error( "Error getting issue transitions.", e );
        }
        Transition toTransition = getTransitionByName( transitions, START_PROGRESS );
        if ( toTransition == null )
        {
            toTransition = getTransitionByName( transitions, REOPEN_ISSUE );
        }
        if ( toTransition == null )
        {
            throw new JiraClientException( "Could not start issue." );
        }
        try
        {
            Issue.FluentTransition fluentTransition = issue.transition();
            fluentTransition.execute( toTransition.getName() );
        }
        catch ( JiraException e )
        {
            logger.error( "Error starting progress on issue", e );
        }
    }


    @Override
    public void resolveIssue( String issueKeyOrId ) throws JiraClientException
    {
        Issue issue = getIssue( issueKeyOrId );

        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );


        try
        {
            Iterable<Transition> transitions = issue.getTransitions();

            Transition toTransition = getTransitionByName( transitions, RESOLVE_ISSUE );

            if ( toTransition == null )
            {
                throw new JiraClientException( "Could not resolve issue." );
            }

            Issue.FluentTransition fluentTransition = issue.transition();
            fluentTransition.execute( toTransition.getName() );
        }
        catch ( JiraException e )
        {
            logger.error( "Error resolving issue", e );
        }
    }


    @Override
    public Iterable<Transition> getTransitions( String issueKeyOrId ) throws JiraClientException
    {
        Issue issue = getIssue( issueKeyOrId );

        logger.debug( String.format( "Current status of %s: %s", issue.getKey(), issue.getStatus() ) );

        try
        {
            return issue.getTransitions();
        }
        catch ( JiraException e )
        {
            throw new JiraClientException( "Error getting issue transitions", e );
        }
    }
}
