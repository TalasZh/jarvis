package jarvis.workflow.plugin.service;


import com.atlassian.annotations.ExperimentalApi;
import com.atlassian.jira.issue.Issue;


public interface JarvisWorkflowValidationService
{
    /*
    * validate transition based on status
    * @param  Issue
    * @param  status
    * @return true|false
    * */
    public boolean validate( Issue issue, String status );

    /*
    * validate if CF page exists
    * @param  Issue
    * @return true|false
    * */
    public boolean validateConfluencePageExists( Issue issue );

    /*
    * check if Issue is locked from transitioning
    * @param  Issue
    * @return true|false
    * */
    @ExperimentalApi
    public boolean isLocked( Issue issue );


    /*
    * lock Issue from changing status
    * @param  Issue
    * @return true|false
    * */
    @ExperimentalApi
    public boolean lock(Issue issue);


 }
