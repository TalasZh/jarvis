package jarvis.workflow.plugin.service.impl;


import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.google.gson.Gson;

import jarvis.workflow.plugin.domain.IssueWrapper;


public class PluginSettingsServiceImpl implements jarvis.workflow.plugin.service.PluginSettingsService
{
    //@formatter:off
    final PluginSettingsFactory pluginSettingsFactory;
    //@formatter:on
    private Gson gson = new Gson();
    private static final Logger log = LoggerFactory.getLogger( PluginSettingsServiceImpl.class );


    public PluginSettingsServiceImpl( final PluginSettingsFactory pluginSettingsFactory )
    {
        this.pluginSettingsFactory = pluginSettingsFactory;
    }


    @Override
    public void storeInfo( String key, String value )
    {
        pluginSettingsFactory.createGlobalSettings().put( PLUGIN_NAMESPACE + key, value );
    }


    @Override
    public String getInfo( String key )
    {
        return ( String ) pluginSettingsFactory.createGlobalSettings().get( PLUGIN_NAMESPACE + key );
    }


    @Override
    public void storeInfo( String projectKey, String key, String value )
    {
        pluginSettingsFactory.createSettingsForKey( projectKey ).put( key, value );
    }


    @Override
    public Object getInfo( String projectKey, String key )
    {
        return pluginSettingsFactory.createSettingsForKey( projectKey ).get( PLUGIN_NAMESPACE + key );
    }


    @Override
    public void storeInfo( final String key, final Collection<Group> collection )
    {
        StringBuffer stringBuffer = new StringBuffer();

        for ( Group group : collection )
        {
            stringBuffer.append( group.getName() + "," );
        }
        pluginSettingsFactory.createGlobalSettings()
                             .put( PLUGIN_NAMESPACE + key, stringBuffer.substring( 0, stringBuffer.length() - 1 ) );
    }


    @Override
    public void storeInfo( final String key, final Object object )
    {
        String json = gson.toJson( object );
        log.warn( "Storing information with key: {} , object: {}", key, json );
        pluginSettingsFactory.createGlobalSettings().put( PLUGIN_NAMESPACE + key, json );
    }


    @Override
    public void storeInfo( final IssueWrapper issueWrapper )
    {
        storeInfo( issueWrapper.getIssueKey(), issueWrapper );
    }
}
