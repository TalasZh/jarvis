package org.safehaus.service;


import com.atlassian.jira.rest.client.api.domain.Issue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.safehaus.analysis.JiraMetricIssue;
import org.safehaus.analysis.JiraMetricIssueKafkaProducer;
import org.safehaus.analysis.service.ConfluenceConnector;
import org.safehaus.analysis.service.JiraConnector;
import org.safehaus.analysis.service.SonarConnector;
import org.safehaus.analysis.service.StashConnector;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.confluence.client.*;
import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.model.Space;
import org.safehaus.jira.JiraClient;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.sonar.model.QuantitativeStats;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.stash.client.Page;
import org.safehaus.stash.model.*;
import org.safehaus.util.DateSave;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;


/**
 * Created by kisik on 30.06.2015.
 */
public class AnalysisService
{
    private final Log log = LogFactory.getLog( AnalysisService.class );

    @Autowired
    private JiraConnector jiraConnector;
    
    @Autowired
    private StashConnector stashConnector;

    @Autowired
    private SonarConnector sonarConnector;
    @Autowired
    private ConfluenceConnector confluenceConnector;

    @Autowired
    private JiraMetricService jiraMetricService;

    List<JiraMetricIssue> jiraMetricIssues;
    List<StashMetricIssue> stashMetricIssues;
    Date lastGatheredJira = null;
    Date lastGatheredStash = null;
    Date lastGatheredSonar = null;
    Date lastGatheredConfluence = null;

    DateSave ds;
    private Page<Project> stashProjects;

    public void run() {
        log.info("Running AnalysisService.run()");


        ds = new DateSave();
        try {
            lastGatheredJira = ds.getLastGatheredDateJira();
            lastGatheredStash = ds.getLastGatheredDateStash();
            lastGatheredSonar = ds.getLastGatheredDateSonar();
            lastGatheredConfluence = ds.getLastGatheredDateConfluence();

            log.info("Last Saved Jira: " + lastGatheredJira.toString());
            log.info("Last Saved Stash: " + lastGatheredStash.toString());
            log.info("Last Saved Sonar: " + lastGatheredSonar.toString());
            log.info("Last Saved Confluence: " + lastGatheredConfluence.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Get Jira Data
        JiraClient jiraCl = null;
        try
        {
            jiraCl = jiraConnector.jiraConnect();
        }
        catch ( JiraClientException e )
        {
            log.error( "Jira Connection couldn't be established" );
            e.printStackTrace();
        }
        if ( jiraCl != null )
        {
            try
            {
                getJiraMetricIssues( jiraCl );
            }
            catch ( Exception ex )
            {
                log.error( "Error pooling issues...", ex );
            }
        }

/*
        // Get Stash Data
        StashManager stashMan = null;
        try {
            stashMan = stashConnector.stashConnect();
        } catch (StashManagerException e) {
            log.error("Stash Connection couldn't be established.");
            e.printStackTrace();
        }
        if(stashMan != null)
            getStashMetricIssues(stashMan);

        // Get Sonar Data
        SonarManager sonarManager = null;
        try
        {
            sonarManager = sonarConnector.sonarConnect();
        }
        catch ( SonarManagerException e )
        {
            log.error( "Sonar Connection couldn't be established." );
            e.printStackTrace();
        }

        if(sonarManager != null)
            getSonarMetricIssues(sonarManager);
*/
        /*
        org.safehaus.confluence.client.ConfluenceManager confluenceManager = null;
        try {
             confluenceManager = confluenceConnector.confluenceConnect();
        } catch ( ConfluenceManagerException e) {
            log.error("Confluence Connection couldn't be established.");
            e.printStackTrace();
        }

        if(confluenceManager != null)
            getConfluenceMetric(confluenceManager);*/

        // Set time.
        try {
            ds.saveLastGatheredDates(lastGatheredJira.getTime(), lastGatheredStash.getTime(), lastGatheredSonar.getTime(), lastGatheredConfluence.getTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getJiraMetricIssues(JiraClient jiraCl)
    {

        List<String> projectKeys = new ArrayList<String>();
        List<Issue> jiraIssues = new ArrayList<Issue>();
        JiraMetricIssueKafkaProducer kafkaProducer = new JiraMetricIssueKafkaProducer();

        jiraMetricIssues = new ArrayList<>();

        // Get all project names to use on getIssues

        try
        {
            List<com.atlassian.jira.rest.client.api.domain.Project> jiraProjects = jiraCl.getAllProjects();
            log.info( "After getAllProjects" );
            log.info( "Printing all projects" );
            for ( com.atlassian.jira.rest.client.api.domain.Project project : jiraProjects )
            {
                projectKeys.add( project.getKey() );
            }
        }
        catch ( Exception e )
        {
            log.error( "Could not get all the projects. {}", e );
            e.printStackTrace();
        }

        try
        {
            log.info( "Printing issues" );
            for ( String projectKey : projectKeys )
            {
                jiraIssues.addAll( jiraCl.getIssues( "'" + projectKey + "'" ) );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        log.info( jiraIssues.size() );


        log.info( "Preparing issues for jarvis format..." );
        for ( Issue issue : jiraIssues )
        {
            JiraMetricIssue issueToAdd = new JiraMetricIssue();
            if ( issue.getKey() != null )
            {
                issueToAdd.setIssueKey( issue.getKey() );
            }
            if ( issue.getId() != null )
            {
                issueToAdd.setIssueId( issue.getId() );
            }
            if ( issue.getStatus() != null && issue.getStatus().getName() != null )
            {
                issueToAdd.setStatus( issue.getStatus().getName() );
            }
            if ( issue.getProject() != null && issue.getProject().getKey() != null )
            {
                issueToAdd.setProjectKey( issue.getProject().getKey() );
            }
            if ( issue.getReporter() != null && issue.getReporter().getName() != null )
            {
                issueToAdd.setReporterName( issue.getReporter().getName() );
            }
            if ( issue.getAssignee() != null && issue.getAssignee().getName() != null )
            {
                issueToAdd.setAssigneeName( issue.getAssignee().getName() );
            }
            if ( issue.getResolution() != null && issue.getResolution().getName() != null )
            {
                issueToAdd.setResolution( issue.getResolution().getName() );
            }
            if ( issue.getCreationDate() != null )
            {
                issueToAdd.setCreationDate( issue.getCreationDate().toDate() );
            }
            if ( issue.getUpdateDate() != null )
            {
                issueToAdd.setUpdateDate( issue.getUpdateDate().toDate() );
            }
            if ( issue.getDueDate() != null )
            {
                issueToAdd.setDueDate( issue.getDueDate().toDate() );
            }
            if ( issue.getPriority() != null && issue.getPriority().getId() != null )
            {
                issueToAdd.setPriority( issue.getPriority().getId() );
            }
            if ( issue.getTimeTracking() != null && issue.getTimeTracking().getOriginalEstimateMinutes() != null )
            {
                issueToAdd.setOriginalEstimateMinutes( issue.getTimeTracking().getOriginalEstimateMinutes() );
            }
            if ( issue.getTimeTracking() != null && issue.getTimeTracking().getRemainingEstimateMinutes() != null )
            {
                issueToAdd.setRemainingEstimateMinutes( issue.getTimeTracking().getRemainingEstimateMinutes() );
            }
            if ( issue.getTimeTracking() != null && issue.getTimeTracking().getTimeSpentMinutes() != null )
            {
                issueToAdd.setTimeSpentMinutes( issue.getTimeTracking().getTimeSpentMinutes() );
            }

            // TODO send for producer
            jiraMetricIssues.add( issueToAdd );
            if(lastGatheredJira != null) {
                if (issueToAdd.getUpdateDate().after(lastGatheredJira)) {
                    // New issue, get it in the database.
                    log.info("Complies, ID:" + issueToAdd.getIssueId() + " UpDate:" + issueToAdd.getUpdateDate());
                    kafkaProducer.send(issueToAdd);

            //            log.info( issueToAdd.toString() );
            //            log.info( "--------------------------------------" );
                } else {
                    // Discard changes because it is already in our database.
                    log.info("Does not, ID:" + issueToAdd.getIssueId() + " UpDate:" + issueToAdd.getUpdateDate());
                }
            }
        }

        log.info( "Getting jira metric service" );
        //        JiraMetricService jiraMetricService = JiraMetricUtil.getService();
        if ( jiraMetricService != null )
        {
            log.info( "Saving issues formatted for jarvis..." );
            log.info( "Performing batch insert" );
            jiraMetricService.batchInsert( jiraMetricIssues );
            //            for ( final JiraMetricIssue jiraIssue : jiraMetricIssues )
            //            {
            //                try
            //                {
            //                    jiraMetricService.insertJiraMetricIssue( jiraIssue );
            //                }
            //                catch ( Exception ex )
            //                {
            //                    log.error( "Error saving jiraMetricIssue to database", ex );
            //                }
            //            }
        }

        // No problem gathering new issues from Jira, which means it should update the last gathering date as of now.
        if(jiraIssues.size() > 0) {
            lastGatheredJira = new Date(System.currentTimeMillis());
        }

    }



    private void getStashMetricIssues( StashManager stashMan )
    {

        List<String> stashProjectKeys = new ArrayList<String>();

        // Get all projects
        try
        {
            stashProjects = stashMan.getProjects( 100, 0 );
        }
        catch ( StashManagerException e )
        {
            e.printStackTrace();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        Set<Project> stashProjectSet = null;
        if ( stashProjects != null )
        {
            stashProjectSet = stashProjects.getValues();
            for ( Project p : stashProjectSet )
            {
                stashProjectKeys.add( p.getKey() );
            }
        }


        List<Page<Repository>> reposList = new ArrayList<>();
        List<Set<Repository>> reposSetList = new ArrayList<>();
        List<Pair<String, String>> projectSlugPairs = new ArrayList<>();

        for ( int i = 0; i < stashProjectKeys.size(); i++ )
        {
            try
            {
                reposList.add( stashMan.getRepos( stashProjectKeys.get( i ), 100, 0 ) );
            }
            catch ( StashManagerException e )
            {
                e.printStackTrace();
            }
            reposSetList.add( reposList.get( i ).getValues() );
            for ( Repository r : reposSetList.get( i ) )
            {
                log.info( r.getSlug() );
                projectSlugPairs.add( new Pair<>( stashProjectKeys.get( i ), r.getSlug() ) );
            }
        }

        Page<Commit> commitPage = null;
        Set<Commit> commitSet = new HashSet<>();
        Set<Change> changeSet = new HashSet<>();
        for ( int i = 0; i < projectSlugPairs.size(); i++ )
        {
            try
            {
                // TODO determine limit value and remove hardcoded number
                commitPage =
                        stashMan.getCommits( projectSlugPairs.get( i ).getL(), projectSlugPairs.get( i ).getR(), 1000,
                                0 );
            }
            catch ( StashManagerException e )
            {
                e.printStackTrace();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            if ( commitPage != null )
            {
                commitSet = commitPage.getValues();
            }

            Page<Change> commitChanges = null;

            for ( Commit commit : commitSet )
            {
                try
                {
                    commitChanges = stashMan.getCommitChanges( projectSlugPairs.get( i ).getL(),
                            projectSlugPairs.get( i ).getR(), commit.getId(), 1000, 0 );
                }
                catch ( StashManagerException e )
                {
                    e.printStackTrace();
                }
                if ( commitChanges != null )
                {
                    changeSet = commitChanges.getValues();
                }
                commitChanges = null;

                for ( Change change : changeSet )
                {
                    log.info( commit.getId() );
                    log.info( commit.getAuthor().getName() );
                    log.info( commit.getDisplayId() );
                    log.info( change.getType() );
                    log.info( change.getContentId() );
                    log.info( change.getNodeType() );
                    log.info( change.getPercentUnchanged() );

                    StashMetricIssue stashMetricIssue = new StashMetricIssue();
                    stashMetricIssue.setPath( change.getPath() );
                    stashMetricIssue.setAuthor( commit.getAuthor() );
                    stashMetricIssue.setAuthorTimestamp( commit.getAuthorTimestamp() );
                    stashMetricIssue.setId( change.getContentId() );
                    stashMetricIssue.setNodeType( change.getNodeType() );
                    stashMetricIssue.setPercentUnchanged( change.getPercentUnchanged() );
                    stashMetricIssue.setProjectName( projectSlugPairs.get( i ).getL() );
                    stashMetricIssue.setSrcPath( change.getSrcPath() );
                    stashMetricIssue.setType( change.getType() );

                    //TODO set stashmetric issue provider over here
                }
            }
        }
    }


    private void getSonarMetricIssues(SonarManager sonarManager) {
        Set<Resource> resources = null;
        List<String> resourceKeys = new ArrayList<>();
        List<Integer> resourceIDs = new ArrayList<>();


        log.info("Get Sonar Metric Issues ici.");
        try {
            resources = sonarManager.getResources();
        } catch (SonarManagerException e) {
            e.printStackTrace();
        }
        if(resources != null) {
            for (Resource r : resources) {
                log.info("Resource:");
                log.info(r.getName());
                log.info(r.getId());
                log.info(r.getKey());
                resourceKeys.add(r.getKey());
                resourceIDs.add(r.getId());
            }
        }
/*
        try {
            QuantitativeStats quantitativeStats = sonarManager.getQuantitativeStats(resourceKeys.get(resourceKeys.size() - 1).toString());
            log.info("LOC: " + quantitativeStats.getLinesOfCode());
        } catch (SonarManagerException e) {
            e.printStackTrace();
        }
*/
    }

    private void getConfluenceMetric(ConfluenceManager confluenceManager) {
        log.info("Get Confluence Metric Issues ici.");

        List<Space> spaceList = null;
        try {
            spaceList = confluenceManager.getAllSpaces();
        } catch (ConfluenceManagerException e) {
            e.printStackTrace();
        }
        List<org.safehaus.confluence.model.Page> pageList = new ArrayList<org.safehaus.confluence.model.Page>();
        for(Space s : spaceList) {
            try {
                pageList.addAll(confluenceManager.listPages(s.getKey(), 0, 100));
            } catch (ConfluenceManagerException e) {
                e.printStackTrace();
            }
        }

        for(org.safehaus.confluence.model.Page p : pageList) {
            log.info(p.getTitle());
            //TODO need to update the cofluence api in order to get the author name.
        }
    }


    class Pair<L, R>
    {
        private L l;
        private R r;


        public Pair( L l, R r )
        {
            this.l = l;
            this.r = r;
        }


        public L getL()
        {
            return l;
        }


        public R getR()
        {
            return r;
        }


        public void setL( L l )
        {
            this.l = l;
        }


        public void setR( R r )
        {
            this.r = r;
        }
    }
}
