package jarvis.workflow.plugin.postfunction;


import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginFunctionFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;


public class JarvisAssigneeChangePostFunctionFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginFunctionFactory
{

    @Override
    protected void getVelocityParamsForInput( Map<String, Object> velocityParams )
    {

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

    }


    public Map<String, ?> getDescriptorParams( Map<String, Object> formParams )
    {
        Map params = new HashMap();


        return params;
    }
}