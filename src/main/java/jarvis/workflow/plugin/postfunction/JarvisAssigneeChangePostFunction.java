package jarvis.workflow.plugin.postfunction;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.UserUtils;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.Gson;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

import jarvis.workflow.plugin.domain.IssueWrapper;
import jarvis.workflow.plugin.service.impl.PluginSettingsServiceImpl;


public class JarvisAssigneeChangePostFunction extends AbstractJiraFunctionProvider
{
    //@formatter:off
    private PluginSettingsServiceImpl service;
    private final UserManager      userManager;
    private final CrowdService     crowdService;
    private final GroupManager     groupManager;
    private final IssueManager     issueService;
    //@formatter:on
    private static final Logger log = LoggerFactory.getLogger( JarvisAssigneeChangePostFunction.class );
    private Gson gson = new Gson();


    public JarvisAssigneeChangePostFunction( PluginSettingsFactory factory, final UserManager userManager,
                                             final CrowdService crowdService, final GroupManager groupManager,
                                             final IssueManager issueService )
    {
        this.userManager = userManager;
        this.crowdService = crowdService;
        this.groupManager = groupManager;
        this.issueService = issueService;
        this.service = new PluginSettingsServiceImpl( factory );
    }


    public void execute( Map transientVars, Map args, PropertySet ps ) throws WorkflowException
    {
        //if issue already exists, retrieve and reassign
        //get mutable issue - strangely you don't have any getters in mutable issue object
        MutableIssue mutableIssue = getIssue( transientVars );

        //Issue issue = getIssue( transientVars );
        IssueWrapper wrapper = getIssue( mutableIssue );

        //issue exists in persistence
        if ( wrapper != null )
        {
            log.warn( "Issue exists : " + wrapper.toString() );
            if ( wrapper.getDirection() == 0 )
            {
                log.warn( "Issue currently assigned to original assignee" );
                User assignee = UserUtils.getUserByEmail( wrapper.getApprovingUser() );
                mutableIssue.setAssignee( assignee );
                mutableIssue.store();
//                issueService.updateIssue( assignee, mutableIssue, EventDispatchOption.ISSUE_UPDATED, true );
                wrapper.setDirection( 1 );
                service.storeInfo( wrapper );
            }
            else
            {
                log.warn( "Issue currently assigned to approving user" );

                User assignee = UserUtils.getUserByEmail( wrapper.getAssignee() );
                mutableIssue.setAssignee( assignee );
                mutableIssue.store();
//                issueService.updateIssue( assignee, mutableIssue, EventDispatchOption.ISSUE_UPDATED, true );
                wrapper.setDirection( 0 );
                service.storeInfo( wrapper );
            }
        }
        else
        {
            firstTimeAssignment( transientVars );
        }
    }


    public void firstTimeAssignment( Map transientVars )
    {
        log.warn( "First time reassignment..." );
        //get mutable issue
        MutableIssue mutableIssue = getIssue( transientVars );
        //get immutable issue
        //Issue issue = getIssue( transientVars );
        //get assigned user
        User user = mutableIssue.getAssigneeUser();
        //get random user from approving group(s)
        User approvingUser = getUserFromApprovingGroup();
        //save current assigned person
        saveIssue( mutableIssue, user, approvingUser, 1 );
        //set new assignee from approving group
        mutableIssue.setAssignee( approvingUser );
        //persist changes
        mutableIssue.store();
//        issueService.updateIssue( approvingUser, mutableIssue, EventDispatchOption.ISSUE_UPDATED, true );

    }


    public User getUserFromApprovingGroup()
    {
        String approvingGroups = ( String ) service.getInfo( service.APPROVING_GROUPS_KEY );

        StringTokenizer stringTokenizer = null;
        log.warn( "Found approving groups: " + approvingGroups );

        List<User> approvingGroupCollection = new ArrayList<>();
        Group group;

        if ( approvingGroups != null )
        {
            stringTokenizer = new StringTokenizer( approvingGroups, "," );
            while ( stringTokenizer.hasMoreTokens() )
            {
                group = groupManager.getGroup( stringTokenizer.nextToken() );
                approvingGroupCollection.addAll( getUsersOfGroup( group ) );
            }
        }
        log.warn( "Selecting single user from : " + approvingGroupCollection.toString() );
        Collections.shuffle( approvingGroupCollection );
        return approvingGroupCollection.get( 0 );
    }


    public Collection<User> getUsersOfGroup( Group group )
    {

        return groupManager.getUsersInGroup( group );
    }

    public IssueWrapper getIssue( Issue issue )
    {
        String jsonIssue = service.getInfo( issue.getKey() );
        IssueWrapper issueWrapper = gson.fromJson( jsonIssue, IssueWrapper.class );
        log.warn( "Json object :{} , issueWrapper: {} ", jsonIssue, issueWrapper );
        return issueWrapper;
    }


    public void saveIssue( Issue issue, User currentUser, User approvingUser, int dir )
    {
        service.storeInfo( issue.getKey(),
                new IssueWrapper( issue.getKey(), currentUser.getEmailAddress(), approvingUser.getEmailAddress(),
                        dir ) );
    }
}