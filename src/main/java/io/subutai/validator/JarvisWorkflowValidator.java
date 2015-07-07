package io.subutai.validator;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;

import io.subutai.exception.JarvisWorkflowException;
import io.subutai.service.JarvisConfluenceService;
import io.subutai.service.impl.JarvisConfluenceServiceImpl;


public class JarvisWorkflowValidator implements Validator
{
    private static final Logger log = LoggerFactory.getLogger( JarvisWorkflowValidator.class );
    private JarvisConfluenceService jarvisConfluenceService;
    private final PluginSettingsFactory factory;


    public JarvisWorkflowValidator( final PluginSettingsFactory factory )
    {

        this.jarvisConfluenceService = new JarvisConfluenceServiceImpl( factory );
        this.factory = factory;
    }


    public void validate( Map transientVars, Map args, PropertySet ps )
            throws InvalidInputException, JarvisWorkflowException
    {
        Issue issue = ( Issue ) transientVars.get( "issue" );
        String statusName = issue.getStatusObject().getSimpleStatus().getName();
        log.warn( "Checking if Confluence Page exists for Issue with id {} and status {}", issue.getId(), statusName );
        if ( !jarvisConfluenceService.confluencePageExists( issue ) )
        {
            throw new JarvisWorkflowException(
                    String.format( "Confluence Page %s %s does not exist", issue.getKey(), statusName ) );
        }
    }
}
