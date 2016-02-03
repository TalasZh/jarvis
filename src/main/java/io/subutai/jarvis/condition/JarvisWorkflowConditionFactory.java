package io.subutai.jarvis.condition;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import io.subutai.jarvis.service.PluginSettingsService;
import io.subutai.jarvis.service.impl.PluginSettingsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginConditionFactory;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.util.collect.MapBuilder;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ConditionDescriptor;


public class JarvisWorkflowConditionFactory extends AbstractWorkflowPluginFactory
        implements WorkflowPluginConditionFactory
{
    //@formatter:off
    private final GroupManager           groupManager;
    private final PluginSettingsService pluginSettingsService;
    //@formatter:on

    private static final Logger log = LoggerFactory.getLogger( JarvisWorkflowConditionFactory.class );


    public JarvisWorkflowConditionFactory( final GroupManager groupManager, final PluginSettingsFactory factory )
    {

        this.groupManager = groupManager;
        pluginSettingsService = new PluginSettingsServiceImpl( factory );
    }


    /*
    * Sets all available groups into a Map, these groups will be available for admin to chose from
    *
    * */
    protected void getVelocityParamsForInput( Map velocityParams )
    {

        Collection<Group> allGroups = groupManager.getAllGroups();

        velocityParams.put( "groups", allGroups );
    }


    protected void getVelocityParamsForEdit( Map velocityParams, AbstractDescriptor descriptor )
    {
        getVelocityParamsForInput( velocityParams );
        velocityParams.put( "selectedGroups", getSelectedGroups( descriptor ) );
        getVelocityParamsForView( velocityParams, descriptor );
    }


    protected void getVelocityParamsForView( Map velocityParams, AbstractDescriptor descriptor )
    {
        Collection selectedGroups = getSelectedGroups( descriptor );
        List selectedGroupsList = new LinkedList();

        for ( Iterator iterator = selectedGroups.iterator(); iterator.hasNext(); )
        {
            String groupName = ( String ) iterator.next();
            Group selectedGroup = groupManager.getGroup( groupName );

            if ( selectedGroup != null )
            {
                selectedGroupsList.add( selectedGroup );
            }
        }
        pluginSettingsService.storeInfo( pluginSettingsService.APPROVING_GROUPS_KEY, selectedGroupsList );

        velocityParams.put( "groups", Collections.unmodifiableCollection( selectedGroupsList ) );
    }


    public Map<String, ?> getDescriptorParams( Map conditionParams )
    {
        Collection groupNames = conditionParams.keySet();

        StringBuffer groupName = new StringBuffer();

        for ( Iterator iterator = groupNames.iterator(); iterator.hasNext(); )
        {
            groupName.append( ( String ) iterator.next() + "," );
        }

        return MapBuilder.build( "groups", groupName.substring( 0, groupName.length() - 1 ) );
    }


    private Collection getSelectedGroups( final AbstractDescriptor descriptor )
    {
        Collection selectedGroups = new LinkedList();
        if ( !( descriptor instanceof ConditionDescriptor ) )
        {
            throw new IllegalArgumentException( "Descriptor must be a ConditionDescriptor." );
        }
        ConditionDescriptor conditionDescriptor = ( ConditionDescriptor ) descriptor;
        String groups = ( String ) conditionDescriptor.getArgs().get( "groups" );
        StringTokenizer st = new StringTokenizer( groups, "," );
        while ( st.hasMoreTokens() )
        {
            selectedGroups.add( st.nextToken() );
        }
        return selectedGroups;
    }
}
