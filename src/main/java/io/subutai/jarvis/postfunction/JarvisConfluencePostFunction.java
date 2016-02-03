package io.subutai.jarvis.postfunction;


import java.util.Map;

import io.subutai.jarvis.service.JarvisConfluenceService;
import io.subutai.jarvis.service.impl.JarvisConfluenceServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.util.json.JSONException;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;


public class JarvisConfluencePostFunction extends AbstractJiraFunctionProvider
{
    private static final Logger log = LoggerFactory.getLogger( JarvisConfluencePostFunction.class );

    private final JarvisConfluenceService jarvisConfluenceService;

    public JarvisConfluencePostFunction( PluginSettingsFactory factory)
    {

        this.jarvisConfluenceService = new JarvisConfluenceServiceImpl( factory);
    }


    public void execute( Map transientVars, Map args, PropertySet ps ) throws WorkflowException
    {
        MutableIssue issue = getIssue( transientVars );
        //get id of project space
        String parentPageId = jarvisConfluenceService.getParentPageId( issue );
        log.warn( "Got parent page id {}", parentPageId );
        //create story for story
        String storyId = null;

        if ( parentPageId != null )
        {
            storyId = jarvisConfluenceService.createConfluencePageForStory( issue, parentPageId );
            log.warn( "Created Story Page with Id {}", storyId );
        }

        try
        {
            if ( storyId != null )
            {

                String requirementsId =
                        jarvisConfluenceService.createConfluencePage( issue.getKey() + " REQUIREMENTS", storyId );

                jarvisConfluenceService.addLabel( requirementsId, "REQUIREMENTS" );

                String designId = jarvisConfluenceService.createConfluencePage( issue.getKey() + " DESIGN", storyId );

                jarvisConfluenceService.addLabel( designId, "DESIGN" );

                String playbookId =
                        jarvisConfluenceService.createConfluencePage( issue.getKey() + " PLAYBOOK", storyId );

                jarvisConfluenceService.addLabel( playbookId, "PLAYBOOK" );
            }
        }
        catch ( JSONException e )
        {
            e.printStackTrace();
        }
    }
}
