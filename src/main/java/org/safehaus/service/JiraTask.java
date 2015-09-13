package org.safehaus.service;


/**
 * Created by tzhamakeev on 5/14/15.
 */
public class JiraTask
{

    private static final String JIRA_URL = "http://test-jira.critical-factor.com";


    private ProjectService projectService;
//    private IssueManager issueManager;


//    public void setProjectManager( final ProjectManager projectManager )
//    {
//        this.projectManager = projectManager;
//    }
//

//    public void setIssueManager( final IssueManager issueManager )
//    {
//        this.issueManager = issueManager;
//    }


    public void run()
    {
//        try
        //        {
        ////            getNewProjects();
        //        }
        //        catch ( JiraClientException e )
        //        {
        //            System.out.println( e.getMessage() );
        //        }
    }


//    protected void getNewProjects() throws JiraClientException
//    {
//        try
//        {
    //            JiraRestClient jiraClient = new JiraRestClientImpl( JIRA_URL, "jirabot", "jira135pwd" );
//            try
//            {
//                List<com.atlassian.jira.rest.client.api.domain.Project> jiraProjects = jiraClient.getAllProjects();
//                List<JarvisProject> jarvisProjects = projectManager.getProjects();
//
//                for ( com.atlassian.jira.rest.client.api.domain.Project project : jiraProjects )
//                {
//                    JarvisProject newProject = new JarvisProject( project.getId(), project.getName(), project.getKey() );
//                    if ( !jarvisProjects.contains( newProject ) )
//                    {
//                        projectManager.saveProject( newProject );
//                    }
//                }
//            }
////            catch ( ProjectExistsException e )
////            {
////                e.printStackTrace();
////            }
//            finally
//            {
//                jiraClient.close();
//            }
//        }
//        catch ( IOException | URISyntaxException e )
//        {
//            throw new JiraClientException( e.getMessage(), e );
//        }
//    }


//    protected void getNewProjectsOld() throws JiraClientException
//    {
//        try
//        {
    //            JiraRestClient jiraClient = new JiraRestClientImpl( JIRA_URL, "jirabot", "jira135pwd" );
//            try
//            {
//                List<Issue> stories = jiraClient.getIssues( "JAR", 1000, 0, "Story" );
//
//                System.out.println( String.format( "Retrieved %d issues.", stories.size() ) );
//                List<JarvisProject> projects = projectManager.getProjects();
//
//                for ( Issue story : stories )
//                {
//                    List<JarvisProject> project = projectManager.search( String.format( "id=%s", story.getKey() ) );
//
//                    System.out.println( String.format( "%s %s %s %s %d %s", story.getId(), story.getStatus(),
//                            story.getDescription(), story.getProject(), projects.size(), project.size() ) );
//
//                    if ( project.isEmpty() )
//                    {
//                        JarvisProject newProject = new JarvisProject( story.getId(), story.getSummary(), story.getKey() );
//                        newProject.setCreated( new Date() );
//                        newProject.setUpdated( new Date() );
//                        newProject.setStatus( ProjectStatus.OPEN );
//
//
//                        for ( Iterator<IssueLink> phaseIterator = story.getIssueLinks().iterator();
//                              phaseIterator.hasNext(); )
//                        {
//                            IssueLink phaseLink = phaseIterator.next();
//
//                            System.out.println( String.format( "\t%s ", phaseLink.getTargetIssueKey() ) );
//
//                            Issue phase = jiraClient.getIssue( phaseLink.getTargetIssueKey() );
//                            System.out.println( String.format( "\t%s ", phase.getSummary() ) );
//
//                            {
//                                try
//                                {
//                                    Phase newPhase = new Phase();
//                                    newPhase.setPhase( phase.getSummary().toUpperCase() );
//                                    newPhase.setCreated( new Date() );
//                                    newPhase.setUpdated( new Date() );
//                                    newProject.addPhase( newPhase );
//
//                                    projectManager.saveProject( newProject );
//                                    System.out.println( "Session successfully saved." );
//
//                                    for ( Iterator<IssueLink> taskIterator = phase.getIssueLinks().iterator();
//                                          taskIterator.hasNext(); )
//                                    {
//
//
//                                        IssueLink taskLink = taskIterator.next();
//                                        Issue task = jiraClient.getIssue( taskLink.getTargetIssueKey() );
//
//                                        if ( !"Task".equals( task.getIssueType().getName() ) )
//                                        {
//                                            continue;
//                                        }
//                                        JarvisIssue newTask = new JarvisIssue();
//                                        newTask.setId( task.getId() );
//                                        newTask.setPhase( newPhase );
//                                        issueManager.saveIssue( newTask );
//                                    }
//                                }
//                                catch ( IllegalArgumentException iae )
//                                {
//                                    System.out.println( "Unknown phase: " + phase.getSummary() );
//                                }
//                            }
//                        }
//                    }
//                    else
//                    {
//                        System.out.println( "Session already exists." );
//                    }
//                }
//            }
//            catch ( ProjectExistsException e )
//            {
//                e.printStackTrace();
//            }
//            finally
//            {
//                jiraClient.close();
//            }
//        }
//        catch ( IOException | URISyntaxException e )
//        {
//            throw new JiraClientException( e.getMessage(), e );
//        }
//    }
}
