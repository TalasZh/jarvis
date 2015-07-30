package org.safehaus.service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.safehaus.analysis.JiraMetricIssue;
import org.safehaus.analysis.service.JiraConnector;
import org.safehaus.analysis.service.SonarConnector;
import org.safehaus.analysis.service.StashConnector;
import org.safehaus.jira.JiraClient;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.stash.client.Page;
import org.safehaus.stash.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by kisik on 30.06.2015.
 */
public class AnalysisService {
    private final Log log = LogFactory.getLog(AnalysisService.class);

    @Autowired
    private JiraConnector jiraConnector;

    @Autowired
    private  StashConnector stashConnector;

    @Autowired
    private SonarConnector sonarConnector;

    List<JiraMetricIssue> jiraMetricIssues;
    List<StashMetricIssue> stashMetricIssues;
    private Page<Project> stashProjects;

    public void run() {
        log.info("Running AnalysisService.run()");
/*
        // Get Jira Data
        JiraClient jiraCl = null;
        try {
            jiraCl = jiraConnector.jiraConnect();
        } catch (JiraClientException e) {
            e.printStackTrace();
        }
        if(jiraCl != null)
            getJiraMetricIssues(jiraCl);
*/
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
*/
        // Get Sonar Data
        SonarManager sonarManager = null;
        try {
            sonarManager = sonarConnector.sonarConnect();
        } catch (SonarManagerException e) {
            log.error("Sonar Connection couldn't be established.");
            e.printStackTrace();
        }

        if(sonarManager != null)
            getSonarMetricIssues();

    }


    private void getJiraMetricIssues(JiraClient jiraCl) {

        List<String> projectKeys = new ArrayList<String>();
        List<Issue> jiraIssues = new ArrayList<Issue>();

        jiraMetricIssues = new ArrayList<>();

        // Get all project names to use on getIssues

        try {
            List<com.atlassian.jira.rest.client.api.domain.Project> jiraProjects = jiraCl.getAllProjects();
            log.error("After getAllProjects");
            for(com.atlassian.jira.rest.client.api.domain.Project project : jiraProjects) {
                log.info(project.getName());
                projectKeys.add(project.getKey());
            }
        } catch (Exception e) {
            log.error("Could not get all the projects.");
            e.printStackTrace();
        }

        try {
            for(String projectKey : projectKeys) {
                log.error(projectKey);
                jiraIssues.addAll(jiraCl.getIssues("'" + projectKey + "'"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(jiraIssues.size());


        for(Issue issue : jiraIssues) {
            JiraMetricIssue issueToAdd = new JiraMetricIssue();
            if(issue.getKey() != null)
                issueToAdd.setKey(issue.getKey());
            if(issue.getId() != null)
                issueToAdd.setId(issue.getId());
            if(issue.getStatus() != null && issue.getStatus().getName() != null)
                issueToAdd.setStatus(issue.getStatus().getName());
            if(issue.getProject() != null && issue.getProject().getKey() != null)
                issueToAdd.setProjectKey(issue.getProject().getKey());
            if(issue.getReporter() != null && issue.getReporter().getName() != null)
                issueToAdd.setReporterName(issue.getReporter().getName());
            if(issue.getAssignee() != null && issue.getAssignee().getName() != null)
                issueToAdd.setAssigneeName(issue.getAssignee().getName());
            if(issue.getResolution() != null && issue.getResolution().getName() != null)
                issueToAdd.setResolution(issue.getResolution().getName());
            if(issue.getCreationDate() != null)
                issueToAdd.setCreationDate(issue.getCreationDate().toDate());
            if(issue.getUpdateDate() != null)
                issueToAdd.setUpdateDate(issue.getUpdateDate().toDate());
            if(issue.getDueDate() != null)
                issueToAdd.setDueDate(issue.getDueDate().toDate());
            if(issue.getPriority() != null && issue.getPriority().getId() != null)
                issueToAdd.setPriority(issue.getPriority().getId());
            if(issue.getTimeTracking() != null && issue.getTimeTracking().getOriginalEstimateMinutes() != null)
                issueToAdd.setOriginalEstimateMinutes(issue.getTimeTracking().getOriginalEstimateMinutes());
            if(issue.getTimeTracking() != null && issue.getTimeTracking().getRemainingEstimateMinutes() != null)
                issueToAdd.setRemainingEstimateMinutes(issue.getTimeTracking().getRemainingEstimateMinutes());
            if(issue.getTimeTracking() != null && issue.getTimeTracking().getTimeSpentMinutes() != null)
                issueToAdd.setTimeSpentMinutes(issue.getTimeTracking().getTimeSpentMinutes());

            // TODO send for producer
            jiraMetricIssues.add(issueToAdd);

            log.info(issueToAdd.getKey());
            log.info(issueToAdd.getId());
            log.info(issueToAdd.getStatus());
            log.info(issueToAdd.getProjectKey());
            log.info(issueToAdd.getReporterName());
            log.info(issueToAdd.getAssigneeName());
            log.info(issueToAdd.getResolution());
            log.info(issueToAdd.getCreationDate());
            log.info(issueToAdd.getUpdateDate());
            log.info(issueToAdd.getDueDate());
            log.info(issueToAdd.getPriority());
            log.info(issueToAdd.getOriginalEstimateMinutes());
            log.info(issueToAdd.getRemainingEstimateMinutes());
            log.info(issueToAdd.getTimeSpentMinutes());
            log.info("--------------------------------------");
        }
    }


    private void getStashMetricIssues(StashManager stashMan) {

        List<String> stashProjectKeys = new ArrayList<String>();

        // Get all projects
        try {
            stashProjects = stashMan.getProjects(100, 0);
        } catch (StashManagerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<Project> stashProjectSet = null;
        if(stashProjects != null) {
            stashProjectSet = stashProjects.getValues();
            for(Project p : stashProjectSet) {
                stashProjectKeys.add(p.getKey());
            }
        }



        List<Page<Repository>> reposList = new ArrayList<>();
        List<Set<Repository>> reposSetList = new ArrayList<>();
        List<Pair<String,String>> projectSlugPairs = new ArrayList<>();

        for(int i = 0; i < stashProjectKeys.size(); i++) {
            try {
                reposList.add(stashMan.getRepos(stashProjectKeys.get(i), 100, 0));
            } catch (StashManagerException e) {
                e.printStackTrace();
            }
            reposSetList.add(reposList.get(i).getValues());
            for(Repository r : reposSetList.get(i)) {
                log.info(r.getSlug());
                projectSlugPairs.add(new Pair<>(stashProjectKeys.get(i), r.getSlug()));
            }
        }

        Page<Commit> commitPage = null;
        Set<Commit> commitSet = new HashSet<>();
        Set<Change> changeSet = new HashSet<>();
        for(int i = 0; i < projectSlugPairs.size(); i++) {
            try {
                // TODO determine limit value and remove hardcoded number
                commitPage = stashMan.getCommits(projectSlugPairs.get(i).getL(), projectSlugPairs.get(i).getR(), 1000, 0);
            } catch (StashManagerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(commitPage != null)
                commitSet = commitPage.getValues();

            Page<Change> commitChanges = null;

            for(Commit commit: commitSet) {
                try {
                    commitChanges = stashMan.getCommitChanges(projectSlugPairs.get(i).getL(), projectSlugPairs.get(i).getR(), commit.getId(), 1000, 0);
                } catch (StashManagerException e) {
                    e.printStackTrace();
                }
                if(commitChanges != null)
                    changeSet = commitChanges.getValues();
                commitChanges = null;

                for(Change change : changeSet) {
                    log.info(commit.getId());
                    log.info(commit.getAuthor().getName());
                    log.info(commit.getDisplayId());
                    log.info(change.getType());
                    log.info(change.getContentId());
                    log.info(change.getNodeType());
                    log.info(change.getPercentUnchanged());

                    StashMetricIssue stashMetricIssue = new StashMetricIssue();
                    stashMetricIssue.setPath(change.getPath());
                    stashMetricIssue.setAuthor(commit.getAuthor());
                    stashMetricIssue.setAuthorTimestamp(commit.getAuthorTimestamp());
                    stashMetricIssue.setId(change.getContentId());
                    stashMetricIssue.setNodeType(change.getNodeType());
                    stashMetricIssue.setPercentUnchanged(change.getPercentUnchanged());
                    stashMetricIssue.setProjectName(projectSlugPairs.get(i).getL());
                    stashMetricIssue.setSrcPath(change.getSrcPath());
                    stashMetricIssue.setType(change.getType());


                    //TODO set stashmetric issue provider over here
                }
            }
        }
    }


    private void getSonarMetricIssues() {
        log.info("Get Sonar Metric Issues ici.");
    }



    class Pair<L,R> {
        private L l;
        private R r;
        public Pair(L l, R r){
            this.l = l;
            this.r = r;
        }
        public L getL(){ return l; }
        public R getR(){ return r; }
        public void setL(L l){ this.l = l; }
        public void setR(R r){ this.r = r; }
    }
}
