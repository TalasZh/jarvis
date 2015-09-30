package org.safehaus.timeline;


import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.safehaus.dao.entities.jira.JarvisLink;
import org.safehaus.dao.entities.jira.JiraIssueChangelog;
import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.jira.JiraProject;
import org.safehaus.service.api.JiraMetricDao;
import org.safehaus.timeline.dao.TimelineDaoImpl;
import org.safehaus.timeline.model.ProgressStatus;
import org.safehaus.timeline.model.StoryTimeline;
import org.safehaus.timeline.model.Structure;
import org.safehaus.timeline.model.StructuredIssue;
import org.safehaus.timeline.model.StructuredProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private TimelineDaoImpl timelineDaoImpl;

    private Map<String, StructuredProject> structuredProjects = Maps.newHashMap();


    public TimelineManager( final JiraMetricDao jiraMetricDao, final TimelineDaoImpl timelineDaoImpl )
    {
        logger.error( "Timeline manager initialized" );
        this.jiraMetricDao = jiraMetricDao;
        this.timelineDaoImpl = timelineDaoImpl;
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

            Map<String, JiraMetricIssue> jiraMetricIssues = getJiraProjectIssues( jiraProject.getKey() );

            Set<StructuredIssue> structuredEpics = getProjectEpics( jiraProject.getKey(), jiraMetricIssues );

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
        return timelineDaoImpl.getProjectByKey( projectKey );
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

                epic.setOpenStatus( new ProgressStatus() );
                epic.setInProgressStatus( new ProgressStatus() );
                epic.setDoneStatus( new ProgressStatus() );
                assignIssueEstimate( epic, jiraMetricIssue );

                List<String> epicStories = getChildIssues( jiraMetricIssue );
                for ( final String story : epicStories )
                {
                    buildStructureIssue( story, epic, jiraMetricIssues );
                }

                epics.add( epic );
            }
        }
        return epics;
    }


    private void assignIssueEstimate( StructuredIssue structuredIssue, JiraMetricIssue issue )
    {
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
        if ( progressStatus != null )
        {
            progressStatus
                    .setOriginalEstimate( progressStatus.getOriginalEstimate() + issue.getOriginalEstimateMinutes() );
            progressStatus.setRemainingRestimate(
                    progressStatus.getRemainingRestimate() + issue.getRemainingEstimateMinutes() );
            progressStatus.setTimeSpent( progressStatus.getTimeSpent() + issue.getTimeSpentMinutes() );
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
                                      final Map<String, JiraMetricIssue> jiraMetricIssues )
    {
        JiraMetricIssue issue = jiraMetricIssues.get( issueKey );
        StructuredIssue structuredIssue =
                new StructuredIssue( issue.getIssueKey(), issue.getIssueId(), issue.getType().getName(),
                        issue.getSummary(), issue.getReporterName(), issue.getReporterName(), issue.getAssigneeName(),
                        issue.getUpdateDate().getTime(), issue.getCreationDate().getTime(), issue.getStatus() );

        structuredIssue.setDoneStatus( new ProgressStatus() );
        structuredIssue.setInProgressStatus( new ProgressStatus() );
        structuredIssue.setOpenStatus( new ProgressStatus() );
        // Set values for current issue progress
        assignIssueEstimate( structuredIssue, issue );

        structuredParent.getIssues().add( structuredIssue );


        List<String> linkedIssues = getChildIssues( issue );
        for ( final String linkedIssue : linkedIssues )
        {
            buildStructureIssue( linkedIssue, structuredIssue, jiraMetricIssues );
        }


        // Sum up overall progress for parent issue overall progress
        sumUpEstimates( structuredIssue, structuredParent );
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
            if ( "Parent".equals( link.getLinkType().getName() )
                    && link.getDirection() == JarvisLink.Direction.OUTWARD )
            {
                switch ( issue.getType().getName() )
                {
                    case "Epic":
                        if ( "Story".equals( link.getType().getName() ) )
                        {
                            linkedIssues.add( link.getLinkDirection().getIssueKey() );
                        }
                        break;
                    case "Story":
                        if ( "Requirement".equals( link.getType().getName() ) )
                        {
                            linkedIssues.add( link.getLinkDirection().getIssueKey() );
                        }
                        break;
                    case "Requirement":
                        if ( "Design".equals( link.getType().getName() ) || "Research"
                                .equals( link.getType().getName() ) )
                        {
                            linkedIssues.add( link.getLinkDirection().getIssueKey() );
                        }
                        break;
                    case "Design":
                    case "Playbook":
                        linkedIssues.add( link.getLinkDirection().getIssueKey() );
                        break;
                }
            }
        }
        return linkedIssues;
    }
}
