package jarvis.workflow.plugin.condition;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.crowd.embedded.api.CrowdService;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.workflow.condition.AbstractJiraCondition;
import com.opensymphony.module.propertyset.PropertySet;


public class JarvisWorkflowCondition extends AbstractJiraCondition
{
    private static final Logger log = LoggerFactory.getLogger( JarvisWorkflowCondition.class );


    private final CrowdService crowdService;
    private final JiraAuthenticationContext ac;


    public JarvisWorkflowCondition( CrowdService crowdService, JiraAuthenticationContext ac )
    {
        this.crowdService = crowdService;
        this.ac = ac;
    }


    public boolean passesCondition( Map transientVars, Map args, PropertySet ps )
    {

        ApplicationUser user = ac.getUser();
        
        assert user != null;

        log.warn( "Calling user :" + user.getUsername() );

        String groupsSelected = ( String ) args.get( "groups" );

        log.warn( "Selected Approving Groups: " + groupsSelected );

        List<String> groups = getGroups( groupsSelected );

        for ( String group : groups )
        {
            if ( crowdService.isUserMemberOfGroup( user.getUsername(), group ) )
            {
                return true;
            }
        }

        return false;
    }


    private List<String> getGroups( String csv )
    {
        ArrayList<String> list = new ArrayList<>();
        StringTokenizer groups = new StringTokenizer( csv, "," );

        while ( groups.hasMoreTokens() )
        {
            list.add( groups.nextToken().toString() );
        }
        return list;
    }
}
