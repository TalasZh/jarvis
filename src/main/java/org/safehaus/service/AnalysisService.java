package org.safehaus.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;

import org.joda.time.DateTime;
import org.safehaus.analysis.ConfluenceMetricKafkaProducer;
import org.safehaus.analysis.JiraMetricIssueKafkaProducer;
import org.safehaus.analysis.SparkDirectKafkaStreamSuite;
import org.safehaus.analysis.StashMetricIssueKafkaProducer;
import org.safehaus.analysis.service.ConfluenceConnector;
import org.safehaus.analysis.service.JiraConnector;
import org.safehaus.analysis.service.SonarConnector;
import org.safehaus.analysis.service.StashConnector;
import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.client.ConfluenceManagerException;
import org.safehaus.confluence.model.ConfluenceMetric;
import org.safehaus.confluence.model.Space;
import org.safehaus.dao.entities.jira.JiraMetricIssue;
import org.safehaus.dao.entities.jira.JiraProject;
import org.safehaus.dao.entities.sonar.SonarMetricIssue;
import org.safehaus.dao.entities.stash.Commit;
import org.safehaus.dao.entities.stash.StashMetricIssue;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraRestClient;
import org.safehaus.persistence.CassandraConnector;
import org.safehaus.service.api.JiraMetricDao;
import org.safehaus.service.api.SonarMetricService;
import org.safehaus.service.api.StashMetricService;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
<<<<<<< HEAD
import org.safehaus.sonar.model.QuantitativeStats;
=======
import org.safehaus.sonar.model.ComplexityStats;
>>>>>>> f5274b2... KMS-326, KMS-327 created SonarMetricIssue table and insert sonar related data implemented. (minor fix)
import org.safehaus.sonar.model.UnitTestStats;
import org.safehaus.sonar.model.ViolationStats;
import org.safehaus.stash.client.Page;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.stash.model.Change;
import org.safehaus.stash.model.Project;
import org.safehaus.stash.model.Repository;
import org.safehaus.timeline.TimelineManager;
import org.safehaus.util.DateSave;
import org.sonar.wsclient.services.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.rcarz.jiraclient.Issue;


/**
 * Created by kisik on 30.06.2015.
 */
public class AnalysisService
{
    private final Log log = LogFactory.getLog( AnalysisService.class );


    public AnalysisService( boolean jira, boolean stash, boolean sonar, boolean confluence )
    {
        this.resetLastGatheredJira = jira;
        this.resetLastGatheredStash = stash;
        this.resetLastGatheredSonar = sonar;
        this.resetLastGatheredConfluence = confluence;

        ds = new DateSave();
    }


    @Autowired
    private JiraConnector jiraConnector;

    @Autowired
    private StashConnector stashConnector;

    @Autowired
    private SonarConnector sonarConnector;

    @Autowired
    private ConfluenceConnector confluenceConnector;

    @Autowired
    private JiraMetricDao jiraMetricDao;

    @Autowired
    private StashMetricService stashMetricService;

    @Autowired
    private TimelineManager timelineManager;

    @Autowired
    private SonarMetricService sonarMetricService;


    Set<JiraMetricIssue> jiraMetricIssues;
    List<StashMetricIssue> stashMetricIssues;
    List<ConfluenceMetric> confluenceMetrics;

    Date lastGatheredJira = null;
    Date lastGatheredStash = null;
    Date lastGatheredSonar = null;
    Date lastGatheredConfluence = null;
    DateSave ds;
    private Page<Project> stashProjects;
    private static boolean streamingStarted = false;
    private static boolean indexCreated = false;

    private static boolean resetLastGatheredJira;
    private static boolean resetLastGatheredStash;
    private static boolean resetLastGatheredSonar;
    private static boolean resetLastGatheredConfluence;


    public void run()
    {
        log.info( "Running AnalysisService.run()" );

        // Reset the time values and get all the data since unix start time if necessary.
        if ( resetLastGatheredJira )
        {
            try
            {
                ds.resetLastGatheredDateJira();
            }
            catch ( BackingStoreException e )
            {
                log.error( e );
            }
        }
        if ( resetLastGatheredStash )
        {
            try
            {
                ds.resetLastGatheredDateStash();
            }
            catch ( BackingStoreException e )
            {
                log.error( e );
            }
        }
        if ( resetLastGatheredSonar )
        {
            try
            {
                ds.resetLastGatheredDateSonar();
            }
            catch ( BackingStoreException e )
            {
                log.error( e );
            }
        }
        if ( resetLastGatheredConfluence )
        {
            try
            {
                ds.resetLastGatheredDateConfluence();
            }
            catch ( BackingStoreException e )
            {
                log.error( e );
            }
        }

        try
        {
            lastGatheredJira = ds.getLastGatheredDateJira();
            lastGatheredStash = ds.getLastGatheredDateStash();
            lastGatheredSonar = ds.getLastGatheredDateSonar();
            lastGatheredConfluence = ds.getLastGatheredDateConfluence();

            log.info( "Last Saved Jira: " + lastGatheredJira.toString() );
            log.info( "Last Saved Stash: " + lastGatheredStash.toString() );
            log.info( "Last Saved Sonar: " + lastGatheredSonar.toString() );
            log.info( "Last Saved Confluence: " + lastGatheredConfluence.toString() );
        }
        catch ( IOException e )
        {
            log.error( e );
        }


        /*
           TODO This creation of the index over here is a workaround,
           because kundera creation of index for jira_metric_issue can
           not be done due to the failure of some other tables creation.
        */
        if ( indexCreated == false )
        {
            try
            {
                CassandraConnector cassandraConnector = CassandraConnector.getInstance();
                cassandraConnector.connect( "localhost" );
                cassandraConnector.executeStatement( "use jarvis;" );
                cassandraConnector
                        .executeStatement( "CREATE INDEX jmi_assignee_idx ON jira_metric_issue (\"assignee_name\");" );
                cassandraConnector.close();
                indexCreated = true;
            }
            catch ( Exception ex )
            {
                log.error( ex );
            }
        }


        // Get Jira Data
        JiraRestClient jiraCl = null;
        try
        {
            jiraCl = jiraConnector.jiraConnect();
        }
        catch ( JiraClientException e )
        {
            log.error( "Jira Connection couldn't be established" + e );
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
        else
        {
            log.error( "JiraClientNull..." );
        }


        // Get Stash Data
        StashManager stashMan = null;
        try
        {
            stashMan = stashConnector.stashConnect();
        }
        catch ( StashManagerException e )
        {
            log.error( "Stash Connection couldn't be established." );
            e.printStackTrace();
        }
        if ( stashMan != null )
        {
            try
            {
                getStashMetricIssues( stashMan );
            }
            catch ( Exception ex )
            {
                log.error( "Error persisting stash metric issues.", ex );
            }
        }
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
        if ( sonarManager != null )
        {
            getSonarMetricIssues( sonarManager );
        }

        //Get Confluence data
        org.safehaus.confluence.client.ConfluenceManager confluenceManager = null;
        try
        {
            confluenceManager = confluenceConnector.confluenceConnect();
        }
        catch ( ConfluenceManagerException e )
        {
            log.error( "Confluence Connection couldn't be established." + e );
            e.printStackTrace();
        }
        if ( confluenceManager != null )
        {
            getConfluenceMetric( confluenceManager );
        }

        // Set time.
        try
        {
            ds.saveLastGatheredDates( lastGatheredJira.getTime(), lastGatheredStash.getTime(),
                    lastGatheredSonar.getTime(), lastGatheredConfluence.getTime() );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        try
        {
            if ( !ds.getIsSparkStarted() )
            {
                System.out.println( "Starting Sparkkk Streaming" );
                SparkDirectKafkaStreamSuite.startStreams();
                ds.saveIsSparkStarted( true );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    private void getJiraMetricIssues( JiraRestClient jiraCl )
    {
        log.info( "getJiraMetricIssues" );
        List<String> projectKeys = new ArrayList<String>();
        List<Issue> jiraIssues = new ArrayList<Issue>();
        JiraMetricIssueKafkaProducer kafkaProducer = new JiraMetricIssueKafkaProducer();

        jiraMetricIssues = Sets.newHashSet();

        // Get all project names to use on getIssues

        try
        {
            List<net.rcarz.jiraclient.Project> jiraProjects = jiraCl.getAllProjects();
            log.info( "Printing all projects" );
            for ( net.rcarz.jiraclient.Project project : jiraProjects )
            {
                projectKeys.add( project.getKey() );
                JiraProject jiraProject = new JiraProject( project.getId(), project.getKey(), project.getAssigneeType(),
                        project.getDescription(), project.getName() );
                jiraMetricDao.saveProject( jiraProject );
            }
        }
        catch ( Exception e )
        {
            log.error( "Could not get all the projects. {}", e );
        }

        try
        {
            log.info( "Printing issues" );
            for ( String projectKey : projectKeys )
            {
                List<Issue> issues = jiraCl.getIssues( projectKey );
                for ( final Issue issue : issues )
                {
                    //                    Issue tmp = jiraCl.getIssue( issue.getKey() );
                    jiraIssues.add( issue );
                }

                //                jiraIssues.addAll( jiraCl.getIssues( "'" + projectKey + "'" ) );
            }
        }
        catch ( Exception e )
        {
            log.error( "Error getting full issue information", e );
        }

        log.info( jiraIssues.size() );


        log.info( "Preparing issues for jarvis format..." );
        for ( final Issue issue : jiraIssues )
        {
            JiraMetricIssue issueToAdd = new JiraMetricIssue( issue );


            jiraMetricIssues.add( issueToAdd );
            jiraMetricDao.insertJiraMetricIssue( issueToAdd );
            if ( lastGatheredJira != null )
            {
                if ( issueToAdd.getUpdateDate().after( lastGatheredJira ) )
                {
                    // New issue, get it in the database.
                    log.info( "Complies, ID:" + issueToAdd.getIssueId() + " UpDate:" + issueToAdd.getUpdateDate() );
                    try
                    {
                        kafkaProducer.send( issueToAdd );
                    }
                    catch ( Exception ex )
                    {
                        log.error( "Error while sending message", ex );
                    }
                }
                else
                {
                    // Discard changes because it is already in our database.
                    log.info( "Does not, ID:" + issueToAdd.getIssueId() + " UpDate:" + issueToAdd.getUpdateDate() );
                }
            }
        }
        kafkaProducer.close();

        log.info( "Getting jira metric service" );
        //        JiraMetricDao jiraMetricDao = JiraMetricUtil.getService();
        if ( jiraMetricDao != null )
        {
            log.info( "Saving issues formatted for jarvis..." );
            log.info( "Performing batch insert" );

            jiraMetricDao.batchInsert( Lists.newArrayList( jiraMetricIssues ) );
        }

        // No problem gathering new issues from Jira, which means it should update the last gathering date as of now.
        if ( jiraIssues.size() > 0 )
        {
            lastGatheredJira = new Date( System.currentTimeMillis() );
        }

        timelineManager.init();
    }


    private void getStashMetricIssues( StashManager stashMan )
    {

        List<String> stashProjectKeys = new ArrayList<String>();
        StashMetricIssueKafkaProducer kafkaProducer = new StashMetricIssueKafkaProducer();
        stashMetricIssues = new ArrayList<>();

        // Get all projects
        try
        {
            stashProjects = stashMan.getProjects( 100, 0 );
        }
        catch ( StashManagerException e )
        {
            log.error( "Stash error", e );
        }
        catch ( Exception e )
        {
            log.error( "Unexpected error", e );
        }

        Set<Project> stashProjectSet;
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
        List<Triple<String, String, String>> projectKeyNameSlugTriples = new ArrayList<>();

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
                projectKeyNameSlugTriples
                        .add( new Triple<String, String, String>( stashProjectKeys.get( i ), r.getProject().getName(),
                                r.getSlug() ) );
            }
        }

        Page<Commit> commitPage = null;
        Set<Commit> commitSet = new HashSet<>();
        Set<Change> changeSet = new HashSet<>();
        for ( int i = 0; i < projectKeyNameSlugTriples.size(); i++ )
        {
            try
            {
                commitPage = stashMan.getCommits( projectKeyNameSlugTriples.get( i ).getL(),
                        projectKeyNameSlugTriples.get( i ).getR(), 1000, 0 );
            }
            catch ( StashManagerException e )
            {
                log.error( "Stash error", e );
            }
            catch ( Exception e )
            {
                log.error( "Unexpected error occurred.", e );
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
                    commitChanges = stashMan.getCommitChanges( projectKeyNameSlugTriples.get( i ).getL(),
                            projectKeyNameSlugTriples.get( i ).getR(), commit.getId(), 1000, 0 );
                }
                catch ( StashManagerException e )
                {
                    e.printStackTrace();
                    log.error( "error pulling commmits messages", e );
                }
                if ( commitChanges != null )
                {
                    changeSet = commitChanges.getValues();
                }
                commitChanges = null;

                for ( Change change : changeSet )
                {
                    StashMetricIssue stashMetricIssue = new StashMetricIssue();
                    stashMetricIssue.setPath( change.getPath() );
                    stashMetricIssue.setAuthor( commit.getAuthor() );

                    stashMetricIssue.setAuthorTimestamp( commit.getAuthorTimestamp() );
                    stashMetricIssue.setId( change.getContentId() );

                    stashMetricIssue.setNodeType( change.getNodeType() );
                    stashMetricIssue.setPercentUnchanged( change.getPercentUnchanged() );
                    stashMetricIssue.setProjectName( projectKeyNameSlugTriples.get( i ).getM() );
                    stashMetricIssue.setProjectKey( projectKeyNameSlugTriples.get( i ).getL() );
                    stashMetricIssue.setSrcPath( change.getSrcPath() );
                    stashMetricIssue.setType( change.getType() );
                    stashMetricIssue.setCommitMessage( commit.getMessage() );

                    //                    log.info( stashMetricIssue.toString() );
                    // if the commit is made after lastGathered date put it in the qualified changes.
                    if ( lastGatheredStash != null )
                    {
                        if ( stashMetricIssue.getAuthorTimestamp() > lastGatheredStash.getTime() )
                        {
                            stashMetricIssues.add( stashMetricIssue );
                        }
                    }
                    stashMetricService.insertStashMetricIssue( stashMetricIssue );
                }

                //                stashMetricService.batchInsert( stashMetricIssues );
            }
        }
        for ( StashMetricIssue smi : stashMetricIssues )
        {
            kafkaProducer.send( smi );
        }


        //        stashMetricService.batchInsert( stashMetricIssues );

        if ( stashMetricIssues.size() > 0 )
        {
            lastGatheredStash = new Date( System.currentTimeMillis() );
        }
        kafkaProducer.close();
    }


    private void getSonarMetricIssues( SonarManager sonarManager )
    {
        Set<Resource> resources = null;
        log.info( "Get Sonar Metric Issues ici." );
        try
        {
            resources = sonarManager.getResources();
            if ( resources != null )
            {
                for ( Resource r : resources )
                {
                    SonarMetricIssue sonarMetricIssue = new SonarMetricIssue();
                    int projectId = r.getId();
                    String projectKey = r.getKey();
                    String projectName = r.getName();

<<<<<<< HEAD
<<<<<<< HEAD
                    UnitTestStats unitTestStats = sonarManager.getUnitTestStats( projectKey );
                    ViolationStats violationStats = sonarManager.getViolationStats( projectKey );
                    QuantitativeStats quantitativeStats = sonarManager.getQuantitativeStats( projectKey );
=======
                    UnitTestStats unitTestStats = sonarManager.getUnitTestStats( projectKey );
                    ViolationStats violationStats = sonarManager.getViolationStats( projectKey );
                    ComplexityStats complexityStats = sonarManager.getComplexityStats( projectKey );
>>>>>>> f5274b2... KMS-326, KMS-327 created SonarMetricIssue table and insert sonar related data implemented. (minor fix)

                    double successPercent = unitTestStats.getSuccessPercent();
                    double failures = unitTestStats.getFailures();
                    double errors = unitTestStats.getErrors();
                    double testCounts = unitTestStats.getFailures();
                    double coveragePercent = unitTestStats.getCoveragePercent();
                    double allIssues = violationStats.getAllIssues();
                    double blockerIssues = violationStats.getBlockerIssues();
                    double criticalIssues = violationStats.getCriticalIssues();
<<<<<<< HEAD
                    double majorIssues = violationStats.getMajorIssues();
                    double classesCount = quantitativeStats.getClasses();
                    double functionsCount = quantitativeStats.getFunctions();
                    double filesCount = quantitativeStats.getFiles();
                    double linesOfCode = quantitativeStats.getLinesOfCode();
=======
                    double successPercent = sonarManager.getUnitTestStats( projectKey ).getSuccessPercent();
                    double failures = sonarManager.getUnitTestStats( projectKey ).getFailures();
                    double errors = sonarManager.getUnitTestStats( projectKey ).getErrors();
                    double testCounts = sonarManager.getUnitTestStats( projectKey ).getFailures();
                    double coveragePercent = sonarManager.getUnitTestStats( projectKey ).getCoveragePercent();
                    double allIssues = sonarManager.getViolationStats( projectKey ).getAllIssues();
                    double blockerIssues = sonarManager.getViolationStats( projectKey ).getBlockerIssues();
                    double criticalIssues = sonarManager.getViolationStats( projectKey ).getCriticalIssues();
                    double classesCount = sonarManager.getComplexityStats( projectKey ).getClassComplexity();
                    double functionsCount = sonarManager.getComplexityStats( projectKey ).getFunctionComplexity();
                    double filesCount = sonarManager.getComplexityStats( projectKey ).getFileComplexity();
=======
                    double classesCount = complexityStats.getClassComplexity();
                    double functionsCount = complexityStats.getFunctionComplexity();
                    double filesCount = complexityStats.getFileComplexity();
>>>>>>> f5274b2... KMS-326, KMS-327 created SonarMetricIssue table and insert sonar related data implemented. (minor fix)

>>>>>>> 496e826... KMS-326, KMS-327 created SonarMetricIssue table and insert sonar related data implemented.

                    sonarMetricIssue.setProjectId( projectId );
                    sonarMetricIssue.setProjectName( projectName );
                    sonarMetricIssue.setSuccessPercent( successPercent );
                    sonarMetricIssue.setFailures( failures );
                    sonarMetricIssue.setErrors( errors );
                    sonarMetricIssue.setTestCount( testCounts );
                    sonarMetricIssue.setCoveragePercent( coveragePercent );
                    sonarMetricIssue.setAllIssues( allIssues );
                    sonarMetricIssue.setBlockerIssues( blockerIssues );
                    sonarMetricIssue.setCriticalIssues( criticalIssues );
                    sonarMetricIssue.setClassesCount( classesCount );
                    sonarMetricIssue.setFunctionsCount( functionsCount );
                    sonarMetricIssue.setFilesCount( filesCount );
<<<<<<< HEAD
                    sonarMetricIssue.setMajorIssues( majorIssues );
                    sonarMetricIssue.setLinesOfCode( linesOfCode );
=======
>>>>>>> 496e826... KMS-326, KMS-327 created SonarMetricIssue table and insert sonar related data implemented.

                    sonarMetricService.insertSonarMetricIssue( sonarMetricIssue );
                }
            }
        }
        catch ( SonarManagerException e )
        {
            e.printStackTrace();
        }
    }


    private void getConfluenceMetric( ConfluenceManager confluenceManager )
    {
        log.info( "getConfluenceMetric" );
        ConfluenceMetricKafkaProducer confluenceMetricKafkaProducer = new ConfluenceMetricKafkaProducer();

        confluenceMetrics = new ArrayList<>();

        List<Space> spaceList = null;
        try
        {
            spaceList = confluenceManager.getAllSpaces();
        }
        catch ( ConfluenceManagerException e )
        {
            log.error( "Confluence Manager Exception ", e );
        }
        List<org.safehaus.confluence.model.Page> pageList = new ArrayList<org.safehaus.confluence.model.Page>();
        if ( spaceList != null )
        {
            for ( Space s : spaceList )
            {
                try
                {
                    pageList.addAll(
                            confluenceManager.listPagesWithOptions( s.getKey(), 0, 100, false, true, true, false ) );
                }
                catch ( ConfluenceManagerException e )
                {
                    log.error( "Confluence Manager Exception ", e );
                }
            }
        }

        for ( org.safehaus.confluence.model.Page p : pageList )
        {
            ConfluenceMetric cf = new ConfluenceMetric();

            cf.setAuthorDisplayName( p.getVersion().getBy().getDisplayName() );
            cf.setAuthorUserKey( p.getVersion().getBy().getUserKey() );
            cf.setAuthorUsername( p.getVersion().getBy().getUsername() );
            cf.setBodyLength( p.getBody().getView().getValue().length() );
            cf.setPageID( Integer.parseInt( p.getId() ) );
            cf.setTitle( p.getTitle() );
            cf.setVersionNumber( p.getVersion().getNumber() );
            cf.setWhen( new DateTime( p.getVersion().getWhen() ).toDate() );

            log.info( "------------------------------------------------" );
            log.info( "PageID:      " + cf.getPageID() );
            log.info( "When:        " + cf.getWhen() );
            log.info( "Number:      " + cf.getVersionNumber() );
            log.info( "Username:    " + cf.getAuthorUsername() );
            log.info( "Displayname: " + cf.getAuthorDisplayName() );
            log.info( "UserKey:     " + cf.getAuthorUserKey() );
            log.info( "BodyLen:     " + cf.getBodyLength() );
            log.info( "Title:       " + cf.getTitle() );

            confluenceMetrics.add( cf );
        }
        for ( ConfluenceMetric cf : confluenceMetrics )
        {
            confluenceMetricKafkaProducer.send( cf );
        }
        confluenceMetricKafkaProducer.close();
    }


    class Triple<L, M, R>
    {
        private L l;
        private R r;
        private M m;


        public Triple( L l, M m, R r )
        {
            this.l = l;
            this.m = m;
            this.r = r;
        }


        public L getL()
        {
            return l;
        }


        public M getM()
        {
            return m;
        }


        public R getR()
        {
            return r;
        }


        public void setL( L l )
        {
            this.l = l;
        }


        public void setM( M m )
        {
            this.m = m;
        }


        public void setR( R r )
        {
            this.r = r;
        }
    }
}
