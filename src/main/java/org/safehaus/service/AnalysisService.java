package org.safehaus.service;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Project;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.safehaus.analysis.JiraMetricIssue;
import org.safehaus.analysis.service.AnalysisConnector;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraClient;
import org.safehaus.model.JarvisContext;
import org.safehaus.sonar.client.SonarManager;
import org.safehaus.sonar.client.SonarManagerException;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.util.JarvisContextHolder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kisik on 30.06.2015.
 */
public class AnalysisService {
    private final Log log = LogFactory.getLog(AnalysisService.class);

    public void run() {
        log.info("Running AnalysisService.run()");

        JiraClient jiraCl = null;
        try {
            jiraCl = AnalysisConnector.jiraConnect();
        } catch(JiraClientException e) {
            log.error("Jira Connection couldn't be established.");
            e.printStackTrace();
        }

        SonarManager sonarMan = null;
        try {
            sonarMan = AnalysisConnector.sonarConnect();
        } catch (SonarManagerException e) {
            log.error("Sonar Connection couldn't be established.");
            e.printStackTrace();
        }

        StashManager stashMan = null;
        try {
            stashMan = AnalysisConnector.stashConnect();
        } catch (StashManagerException e) {
            log.error("Stash Connection couldn't be established.");
            e.printStackTrace();
        }

        List<Project> jiraProjects = jiraCl.getAllProjects();
        for(Project project : jiraProjects) {
            log.info(project.getName());
            log.info(project.getKey());
            log.info("***************************");
        }

        List<Issue> jiraIssues = null;
        try {
            jiraIssues = jiraCl.getIssues("JAR");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(jiraIssues.size());

        List<JiraMetricIssue> merticIssueList = new ArrayList();
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

            merticIssueList.add(issueToAdd);

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
}
