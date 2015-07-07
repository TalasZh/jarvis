package io.subutai.validator;

import com.atlassian.core.util.map.EasyMap;
import com.google.common.collect.Maps;
import com.opensymphony.workflow.loader.AbstractDescriptor;
import com.opensymphony.workflow.loader.ValidatorDescriptor;
import com.atlassian.jira.plugin.workflow.WorkflowPluginValidatorFactory;
import com.atlassian.jira.plugin.workflow.AbstractWorkflowPluginFactory;
import java.util.Map;

public class JarvisWorkflowValidatorFactory extends AbstractWorkflowPluginFactory implements WorkflowPluginValidatorFactory
{
    protected void getVelocityParamsForInput( Map velocityParams )
    {

    }


    protected void getVelocityParamsForEdit( Map velocityParams, AbstractDescriptor descriptor )
    {

    }


    protected void getVelocityParamsForView( Map velocityParams, AbstractDescriptor descriptor )
    {

    }


    public Map getDescriptorParams( Map conditionParams )
    {
        return Maps.newHashMap();
    }

}
