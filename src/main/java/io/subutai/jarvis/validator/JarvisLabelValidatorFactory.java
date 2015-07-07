package io.subutai.jarvis.validator;


import java.util.Map;

import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.google.common.collect.Maps;
import com.opensymphony.workflow.loader.AbstractDescriptor;


public class JarvisLabelValidatorFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory
{

    protected void getVelocityParamsForInput( Map velocityParams )
    {

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
        return Maps.newHashMap();
    }
}
