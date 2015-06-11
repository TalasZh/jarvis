package jarvis.workflow.plugin.validator;


import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;

import jarvis.workflow.plugin.exception.JarvisWorkflowException;


public class JarvisWorkflowValidator implements Validator
{
    private static final Logger log = LoggerFactory.getLogger( JarvisWorkflowValidator.class );


    public void validate( Map transientVars, Map args, PropertySet ps )
            throws InvalidInputException, JarvisWorkflowException
    {
        Issue issue = ( Issue ) transientVars.get( "issue" );
        String statusName = issue.getStatusObject().getSimpleStatus().getName();
        log.warn( "Checking if validations are met for Issue with id {} and status {}", issue.getId(), statusName );
        boolean allowTransition = new Random().nextBoolean();
        log.warn( "Confluence page exists for Issue with id {} , {}", issue.getId(), false );

        if ( !allowTransition )
        {
            throw new JarvisWorkflowException( "Cannot Transition :Issue must have completed Confluence Page" );
        }
    }
}
