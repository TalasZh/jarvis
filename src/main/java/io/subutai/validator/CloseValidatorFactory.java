package io.subutai.validator;


import java.util.HashMap;
import java.util.Map;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.opensymphony.workflow.loader.AbstractDescriptor;


public class CloseValidatorFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory
{

    protected void getVelocityParamsForInput( Map velocityParams )
    {
        //the default message

    }


    protected void getVelocityParamsForEdit( Map velocityParams, AbstractDescriptor descriptor )
    {
        getVelocityParamsForInput( velocityParams );
        getVelocityParamsForView( velocityParams, descriptor );
    }


    protected void getVelocityParamsForView( Map velocityParams, AbstractDescriptor descriptor )
    {

    }


    public Map getDescriptorParams( Map validatorParams )
    {
        return new HashMap();
    }
}
