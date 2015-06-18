package jarvis.workflow.plugin.postfunction;


import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.FunctionDescriptor;

import jarvis.workflow.plugin.service.PluginSettingsService;
import jarvis.workflow.plugin.service.impl.PluginSettingsServiceImpl;
import webwork.action.ActionContext;


public class JarvisConfluencePostFunctionFactory extends AbstractWorkflowPluginFactory
        implements WorkflowPluginFunctionFactory
{


    public static final String URL_FIELD = "confluenceUrl";
    public static final String PROJECT_KEY_SPACE_FIELD = "projectKeySpace";
    private final PluginSettingsService pluginSettingsService;

    private WorkflowManager workflowManager;


    public JarvisConfluencePostFunctionFactory( final PluginSettingsFactory factory, WorkflowManager workflowManager )
    {
        this.pluginSettingsService = new PluginSettingsServiceImpl( factory );
        this.workflowManager = workflowManager;
    }


    @Override
    protected void getVelocityParamsForInput( Map<String, Object> velocityParams )
    {

//        Map<String, String[]> myParams = ActionContext.getParameters();
//
//        final JiraWorkflow jiraWorkflow = workflowManager.getWorkflow( myParams.get( "workflowName" )[0] );
//
//        //the default message
//        velocityParams.put( FIELD_MESSAGE, "Workflow Last Edited By " + jiraWorkflow.getUpdateAuthorName() );
//
    }


    @Override
    protected void getVelocityParamsForEdit( Map<String, Object> velocityParams, AbstractDescriptor descriptor )
    {

        getVelocityParamsForInput( velocityParams );
        getVelocityParamsForView( velocityParams, descriptor );
    }


    @Override
    protected void getVelocityParamsForView( Map<String, Object> velocityParams, AbstractDescriptor descriptor )
    {
        if ( !( descriptor instanceof FunctionDescriptor ) )
        {
            throw new IllegalArgumentException( "Descriptor must be a FunctionDescriptor." );
        }

        FunctionDescriptor functionDescriptor = ( FunctionDescriptor ) descriptor;

        String url = ( String ) functionDescriptor.getArgs().get( URL_FIELD );
        String project = ( String ) functionDescriptor.getArgs().get( PROJECT_KEY_SPACE_FIELD );

        velocityParams.put( URL_FIELD, url );
        velocityParams.put( PROJECT_KEY_SPACE_FIELD, project );
        pluginSettingsService.storeInfo( URL_FIELD, url );
        pluginSettingsService.storeInfo( PROJECT_KEY_SPACE_FIELD, project );
    }


    public Map<String, ?> getDescriptorParams( Map<String, Object> formParams )
    {
        Map params = new HashMap();

        // Process The map
        String url = extractSingleParam( formParams, URL_FIELD );
        String project = extractSingleParam( formParams, PROJECT_KEY_SPACE_FIELD );
        params.put( URL_FIELD, url );
        params.put( PROJECT_KEY_SPACE_FIELD, project );

        return params;
    }
}