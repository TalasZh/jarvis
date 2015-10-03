package org.safehaus.timeline;


import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.safehaus.dao.entities.jira.ChangeCompositeKey;
import org.safehaus.dao.entities.jira.JarvisLink;
import org.safehaus.dao.entities.jira.JiraIssueChangelog;
import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.jira.JiraProject;
import org.safehaus.dao.entities.sonar.SonarMetricIssue;
import org.safehaus.model.Capture;
import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;
import org.safehaus.service.api.JiraMetricDao;
import org.safehaus.service.api.SessionManager;
import org.safehaus.service.api.SonarMetricService;
import org.safehaus.timeline.dao.TimelineDao;
import org.safehaus.timeline.model.ProgressStatus;
import org.safehaus.timeline.model.ProjectStats;
import org.safehaus.timeline.model.StoryPoints;
import org.safehaus.timeline.model.StoryTimeline;
import org.safehaus.timeline.model.Structure;
import org.safehaus.timeline.model.StructuredIssue;
import org.safehaus.timeline.model.StructuredProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


/**
 * Created by talas on 9/29/15.
 */
//@Service
public class TimelineManager
{
    private static final Logger logger = LoggerFactory.getLogger( TimelineManager.class );

    private JiraMetricDao jiraMetricDao;

    private TimelineDao timelineDaoImpl;

    private SessionManager sessionManager;

    @Autowired
    private SonarMetricService sonarMetricService;

    private Map<String, StructuredProject> structuredProjects = Maps.newHashMap();


    public TimelineManager( final JiraMetricDao jiraMetricDao, final TimelineDao timelineDaoImpl )
    {
        logger.error( "Timeline manager initialized" );
        this.jiraMetricDao = jiraMetricDao;
        this.timelineDaoImpl = timelineDaoImpl;
    }


    @Autowired
    public void setSessionManager( final SessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }


    @PostConstruct
    public void init()
    {
        logger.info( "Timeline service initialized." );
        List<JiraProject> jiraProjects = jiraMetricDao.getProjects();
        for ( final JiraProject jiraProject : jiraProjects )
        {
            StructuredProject project =
                    new StructuredProject( jiraProject.getProjectId(), jiraProject.getName(), jiraProject.getKey() );
            project.setDoneStatus( new ProgressStatus() );
            project.setInProgressStatus( new ProgressStatus() );
            project.setOpenStatus( new ProgressStatus() );

            SonarMetricIssue sonarMetricIssue = sonarMetricService.findSonarMetricIssueByProjectId( "5855" );


            ProjectStats projectStats = new ProjectStats( sonarMetricIssue );
            project.setProjectStats( projectStats );

            Map<String, JiraMetricIssue> jiraMetricIssues = getJiraProjectIssues( jiraProject.getKey() );

            Set<StructuredIssue> structuredEpics = getProjectEpics( jiraProject.getKey(), jiraMetricIssues );
            project.setEpicsCount( structuredEpics.size() );

            for ( final StructuredIssue structuredEpic : structuredEpics )
            {
                sumUpEstimates( structuredEpic, project );
            }

            project.setIssues( structuredEpics );
            structuredProjects.put( jiraProject.getKey(), project );

            timelineDaoImpl.updateProject( project );
        }
    }


    /**
     * Constructs project dependency tree
     *
     * @param projectKey - target project key to view dependency
     *
     * @return - project
     */
    public StructuredProject getProject( final String projectKey )
    {
        StructuredProject structuredProject = timelineDaoImpl.getProjectByKey( projectKey );
        if ( structuredProject != null )
        {
            for ( final String issueKey : structuredProject.getIssuesKeys() )
            {
                StructuredIssue issue = timelineDaoImpl.getStructuredIssueByKey( issueKey );
                structuredProject.addIssue( issue );
            }
        }
        return structuredProject;
    }


    /**
     * returns list of projects
     */
    public List<StructuredProject> getProjects()
    {
        return timelineDaoImpl.getAllProjects();
    }


    /**
     * Gets all jira issues for project by putting each into map to make it accessible via issue key
     *
     * @param projectKey - target project to pull issues for
     *
     * @return - issues map
     */
    private Map<String, JiraMetricIssue> getJiraProjectIssues( String projectKey )
    {
        Map<String, JiraMetricIssue> jiraMetricIssues = Maps.newHashMap();

        List<JiraMetricIssue> issues = jiraMetricDao.getProjectIssues( projectKey );
        for ( final JiraMetricIssue issue : issues )
        {
            jiraMetricIssues.put( issue.getIssueKey(), issue );
        }
        return jiraMetricIssues;
    }


    /**
     * Gets all epics for project
     */
    private Set<StructuredIssue> getProjectEpics( String projectKey,
                                                  final Map<String, JiraMetricIssue> jiraMetricIssues )
    {
        Set<StructuredIssue> epics = Sets.newHashSet();
        for ( final Map.Entry<String, JiraMetricIssue> entry : jiraMetricIssues.entrySet() )
        {
            final JiraMetricIssue jiraMetricIssue = entry.getValue();
            if ( "Epic".equals( jiraMetricIssue.getType().getName() ) && projectKey
                    .equals( jiraMetricIssue.getProjectKey() ) )
            {
                StructuredIssue epic = new StructuredIssue( jiraMetricIssue.getIssueKey(), jiraMetricIssue.getIssueId(),
                        jiraMetricIssue.getType().getName(), jiraMetricIssue.getSummary(),
                        jiraMetricIssue.getReporterName(), jiraMetricIssue.getReporterName(),
                        jiraMetricIssue.getAssigneeName(), jiraMetricIssue.getUpdateDate().getTime(),
                        jiraMetricIssue.getCreationDate().getTime(), jiraMetricIssue.getStatus() );

                assignIssueEstimate( epic, jiraMetricIssue );

                List<String> epicStories = getChildIssues( jiraMetricIssue );
                Set<String> issueKeys = Sets.newHashSet();
                for ( final String story : epicStories )
                {
                    buildStructureIssue( story, epic, jiraMetricIssues, issueKeys );
                }

                epics.add( epic );
            }
        }
        return epics;
    }


    private void assignIssueEstimate( StructuredIssue structuredIssue, JiraMetricIssue issue )
    {
        structuredIssue.setOpenStatus( new ProgressStatus() );
        structuredIssue.setInProgressStatus( new ProgressStatus() );
        structuredIssue.setDoneStatus( new ProgressStatus() );

        structuredIssue.getUsers().add( structuredIssue.getReporter() );

        ProgressStatus progressStatus = null;
        switch ( issue.getStatus() )
        {
            case "Open":
                progressStatus = structuredIssue.getOpenStatus();
                break;
            case "In Progress":
                progressStatus = structuredIssue.getInProgressStatus();
                break;
            case "Done":
                progressStatus = structuredIssue.getDoneStatus();
                break;
        }

        if ( "Story".equals( structuredIssue.getIssueType() ) )
        {
            StoryPoints storyPoints = new StoryPoints();
            Random random = new Random();
            long val = ( random.nextInt( 4 ) + 1 ) * 2;
            switch ( issue.getStatus() )
            {
                case "Open":
                    storyPoints.setOpen( val );
                    break;
                case "In Progress":
                    storyPoints.setInProgress( val );
                    break;
                case "Done":
                    storyPoints.setDone( val );
                    break;
            }
            structuredIssue.setStoryPoints( storyPoints );
        }

        if ( progressStatus != null )
        {
            progressStatus
                    .setOriginalEstimate( progressStatus.getOriginalEstimate() + issue.getOriginalEstimateMinutes() );
            progressStatus.setRemainingRestimate(
                    progressStatus.getRemainingRestimate() + issue.getRemainingEstimateMinutes() );
            progressStatus.setTimeSpent( progressStatus.getTimeSpent() + issue.getTimeSpentMinutes() );
        }
        if ( "Resolved".equals( issue.getStatus() ) || "Closed".equals( issue.getStatus() ) ||
                "Done".equals( issue.getStatus() ) )
        {
            String type = issue.getType().getName();
            Long totalSolved = structuredIssue.getTotalIssuesSolved().get( type );
            if ( totalSolved == null )
            {
                totalSolved = 0L;
            }
            totalSolved += issue.getTimeSpentMinutes();
            structuredIssue.getTotalIssuesSolved().put( type, totalSolved );
        }
    }


    private void sumUpEstimates( Structure structuredIssue, Structure parent )
    {
        // Assign open statuses
        sumUpProgresses( structuredIssue.getOpenStatus(), parent.getOpenStatus() );

        // Assign in progress statuses
        sumUpProgresses( structuredIssue.getInProgressStatus(), parent.getInProgressStatus() );

        // Assign done statuses
        sumUpProgresses( structuredIssue.getDoneStatus(), parent.getDoneStatus() );

        StoryPoints childStoryPoints = parent.getStoryPoints();
        StoryPoints parentStoryPoints = structuredIssue.getStoryPoints();

        parentStoryPoints.setDone( parentStoryPoints.getDone() + childStoryPoints.getDone() );
        parentStoryPoints.setInProgress( parentStoryPoints.getInProgress() + childStoryPoints.getInProgress() );
        parentStoryPoints.setOpen( parentStoryPoints.getOpen() + childStoryPoints.getOpen() );

        parent.getUsers().addAll( structuredIssue.getUsers() );

        for ( final Map.Entry<String, Long> entry : structuredIssue.getTotalIssuesSolved().entrySet() )
        {
            String key = entry.getKey();
            Long value = entry.getValue();

            Long parentValue = parent.getTotalIssuesSolved().get( key );
            if ( parentValue == null )
            {
                parentValue = 0L;
            }
            parentValue += value;

            parent.getTotalIssuesSolved().put( key, parentValue );
        }
    }


    private void sumUpProgresses( ProgressStatus progressStatus, ProgressStatus parentProgress )
    {
        if ( progressStatus != null && parentProgress != null )
        {
            parentProgress
                    .setOriginalEstimate( progressStatus.getOriginalEstimate() + parentProgress.getOriginalEstimate() );
            parentProgress.setRemainingRestimate(
                    progressStatus.getRemainingRestimate() + parentProgress.getRemainingRestimate() );
            parentProgress.setTimeSpent( progressStatus.getTimeSpent() + parentProgress.getTimeSpent() );
        }
    }


    /**
     * constructs story timeline according to dependency in issues
     */
    public StoryTimeline getStoryTimeline( final String storyKey, final String fromDate, final String toDate )
    {
        StoryTimeline storyTimeline = new StoryTimeline();
        if ( storyKey != null )
        {
            String projectKey = storyKey.split( "-" )[0];

            Map<String, JiraMetricIssue> jiraMetricIssues = getJiraProjectIssues( projectKey );

            storyTimeline = new StoryTimeline( jiraMetricIssues.get( storyKey ) );
            StructuredIssue story = timelineDaoImpl.getStructuredIssueByKey( storyKey );

            Long from = Long.valueOf( fromDate );
            Long to = Long.valueOf( toDate );

            populateEvents( story, storyTimeline, from, to, jiraMetricIssues );

            //            story.getIssues().remove( (JiraMetricIssue)story );
            storyTimeline.getIssues().remove( storyTimeline );
        }
        return storyTimeline;
    }


    /**
     * populating events for story which are pulled from child issues for selected story
     */
    private void populateEvents( StructuredIssue issue, StoryTimeline storyTimeline, Long fromDate, Long toDate,
                                 final Map<String, JiraMetricIssue> jiraMetricIssues )
    {
        JiraMetricIssue jiraMetricIssue = jiraMetricIssues.get( issue.getKey() );
        if ( jiraMetricIssue != null )
        {


            for ( final JiraIssueChangelog changelog : jiraMetricIssue.getChangelogList() )
            {
                Long eventDate = changelog.getChangeKey().getCreated();
                if ( fromDate < eventDate && eventDate < toDate )
                {
                    try
                    {
                        Session researchSession = sessionManager.getSession( jiraMetricIssue.getIssueKey() );
                        //TODO temporal workaround to serve session captures
                        for ( final Capture capture : researchSession.getCaptures() )
                        {
                            ChangeCompositeKey changeKey =
                                    new ChangeCompositeKey( capture.getId(), capture.getCreated().getTime() );

                            String captureSample =
                                    String.format( "{url:%s,quote:%s,comment:%s}", capture.getUri(), capture.getQuote(),
                                            capture.getText() );

                            JiraIssueChangelog issueChangelog =
                                    new JiraIssueChangelog( changeKey, jiraMetricIssue.getIssueKey(),
                                            jiraMetricIssue.getIssueId(), jiraMetricIssue.getAssigneeName(),
                                            jiraMetricIssue.getType().getName(), "Research Session",
                                            "Annotation Created", captureSample, "", "" );
                            jiraMetricIssue.getChangelogList().add( issueChangelog );
                        }
                    }
                    catch ( SessionNotFoundException e )
                    {
                        logger.error( "No Research Session for issue {}", jiraMetricIssue.getIssueKey() );
                    }
                    catch ( Exception e )
                    {
                        logger.error( "Couldn't retrieve research session for key " + jiraMetricIssue.getIssueKey(),
                                e );
                    }
                    storyTimeline.getIssues().add( jiraMetricIssue );
                    break;
                }
            }
        }
        for ( final StructuredIssue structuredIssue : issue.getIssues() )
        {
            populateEvents( structuredIssue, storyTimeline, fromDate, toDate, jiraMetricIssues );
        }
    }


    /**
     * Constructs dependency tree view for target issue
     */
    private void buildStructureIssue( String issueKey, StructuredIssue structuredParent,
                                      final Map<String, JiraMetricIssue> jiraMetricIssues, final Set<String> issueKeys )
    {
        JiraMetricIssue issue = jiraMetricIssues.get( issueKey );
        if ( issue != null && !issueKeys.contains( issueKey ) )
        {
            issueKeys.add( issueKey );
            StructuredIssue structuredIssue =
                    new StructuredIssue( issue.getIssueKey(), issue.getIssueId(), issue.getType().getName(),
                            issue.getSummary(), issue.getReporterName(), issue.getReporterName(),
                            issue.getAssigneeName(), issue.getUpdateDate().getTime(), issue.getCreationDate().getTime(),
                            issue.getStatus() );

            // Set values for current issue progress
            assignIssueEstimate( structuredIssue, issue );

            structuredParent.addIssue( structuredIssue );

            List<String> linkedIssues = getChildIssues( issue );
            for ( final String linkedIssue : linkedIssues )
            {
                buildStructureIssue( linkedIssue, structuredIssue, jiraMetricIssues, issueKeys );
            }

            // Sum up overall progress for parent issue overall progress
            sumUpEstimates( structuredIssue, structuredParent );
        }
    }


    /**
     * This method selectively chooses issues from its links and returns list of issue keys which are relevant for
     * structure
     */
    private List<String> getChildIssues( JiraMetricIssue issue )
    {
        List<String> linkedIssues = Lists.newArrayList();
        for ( final JarvisLink link : issue.getIssueLinks() )
        {
            if ( link.getDirection() == JarvisLink.Direction.OUTWARD )
            {
                linkedIssues.add( link.getLinkDirection().getIssueKey() );
            }
        }
        return linkedIssues;
    }
}
