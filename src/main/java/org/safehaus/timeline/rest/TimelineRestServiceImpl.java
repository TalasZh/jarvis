package org.safehaus.timeline.rest;


import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebService;

import org.safehaus.dao.entities.jira.JarvisLink;
import org.safehaus.dao.entities.jira.JiraIssueChangelog;
import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.jira.JiraProject;
import org.safehaus.service.api.JiraMetricDao;
import org.safehaus.timeline.StoryTimeline;
import org.safehaus.timeline.StructuredIssue;
import org.safehaus.timeline.StructuredProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


/**
 * Created by talas on 9/27/15.
 */
@Service( "timelineManager" )
@WebService( serviceName = "TimelineManagerImpl",
        endpointInterface = "org.safehaus.timeline.rest.TimelineRestService" )
public class TimelineRestServiceImpl implements TimelineRestService
{
    @Autowired
    private JiraMetricDao jiraMetricDao;

    private Map<String, StructuredProject> structuredProjects = Maps.newHashMap();

    private Map<String, JiraMetricIssue> jiraMetricIssues = Maps.newHashMap();


    @Override
    public StructuredProject getProject( final String projectKey )
    {
        List<JiraMetricIssue> issues = jiraMetricDao.getProjectIssues( projectKey );
        for ( final JiraMetricIssue issue : issues )
        {
            jiraMetricIssues.put( issue.getIssueKey(), issue );
        }
        List<StructuredIssue> structuredEpics = Lists.newArrayList( getProjectEpics( projectKey ) );

        getProjects();

        StructuredProject structuredProject = structuredProjects.get( projectKey );
        if ( structuredProject != null )
        {
            structuredProject.setIssues( structuredEpics );
        }
        structuredProjects.put( projectKey, structuredProject );

        return structuredProject;
    }


    @Override
    public List<StructuredProject> getProjects()
    {
        //TODO need to optimize requests and cache POJOs
        List<JiraProject> jiraProjects = jiraMetricDao.getProjects();
        for ( final JiraProject jiraProject : jiraProjects )
        {
            StructuredProject project =
                    new StructuredProject( jiraProject.getProjectId(), jiraProject.getName(), jiraProject.getKey() );
            structuredProjects.put( jiraProject.getKey(), project );
        }
        return Lists.newArrayList( structuredProjects.values() );
    }


    private Set<StructuredIssue> getProjectEpics( String projectKey )
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
                        jiraMetricIssue.getAssigneeName(), jiraMetricIssue.getUpdateDate() );

                List<String> epicStories = getChildIssues( jiraMetricIssue );
                for ( final String story : epicStories )
                {
                    buildStructureIssue( story, epic );
                }

                epics.add( epic );
            }
        }
        return epics;
    }


    @Override
    public StoryTimeline getStoryTimeline( final String storyKey, final String fromDate, final String toDate )
    {
        StoryTimeline storyTimeline = new StoryTimeline();
        if ( storyKey != null )
        {
            String projectKey = storyKey.split( "-" )[0];
            StructuredProject project = getProject( projectKey );
            StructuredIssue story = findIssueInStructure( project.getIssues(), storyKey );
            Long from = Long.valueOf( fromDate );
            Long to = Long.valueOf( toDate );
            populateEvents( story, storyTimeline, from, to );
        }
        return storyTimeline;
    }


    private void populateEvents( StructuredIssue issue, StoryTimeline storyTimeline, Long fromDate, Long toDate )
    {
        JiraMetricIssue jiraMetricIssue = jiraMetricIssues.get( issue.getKey() );
        if ( jiraMetricIssue != null )
        {
            for ( final JiraIssueChangelog changelog : jiraMetricIssue.getChangelogList() )
            {
                Long eventDate = changelog.getChangeKey().getCreated();
                if ( fromDate < eventDate && eventDate < toDate )
                {
                    storyTimeline.getIssuesEvents().add( changelog );
                }
            }
        }
        for ( final StructuredIssue structuredIssue : issue.getIssues() )
        {
            populateEvents( structuredIssue, storyTimeline, fromDate, toDate );
        }
    }


    private StructuredIssue findIssueInStructure( List<StructuredIssue> issues, String issueKey )
    {
        for ( final StructuredIssue structuredIssue : issues )
        {
            if ( issueKey.equals( structuredIssue.getKey() ) )
            {
                return structuredIssue;
            }
            else
            {
                StructuredIssue result = findIssueInStructure( structuredIssue.getIssues(), issueKey );
                if ( result != null )
                {
                    return result;
                }
            }
        }
        return null;
    }


    private void buildStructureIssue( String issueKey, StructuredIssue structuredParent )
    {
        JiraMetricIssue issue = jiraMetricIssues.get( issueKey );
        StructuredIssue structuredIssue =
                new StructuredIssue( issue.getIssueKey(), issue.getIssueId(), issue.getType().getName(),
                        issue.getSummary(), issue.getReporterName(), issue.getReporterName(), issue.getAssigneeName(),
                        issue.getUpdateDate() );

        structuredParent.getIssues().add( structuredIssue );

        for ( final JarvisLink link : issue.getIssueLinks() )
        {
            List<String> linkedIssues = getChildIssues( issue );
            for ( final String linkedIssue : linkedIssues )
            {
                buildStructureIssue( linkedIssue, structuredIssue );
            }
        }
    }


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
