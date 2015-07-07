package io.subutai.validator;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.label.Label;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;

import io.subutai.exception.JarvisWorkflowException;


public class JarvisLabelValidator implements Validator
{
    private static final Logger log = LoggerFactory.getLogger( JarvisLabelValidator.class );
    private final IssueLinkManager manager;


    public JarvisLabelValidator( final IssueLinkManager manager )
    {
        this.manager = manager;
    }


    public void validate( Map transientVars, Map args, PropertySet ps )
            throws InvalidInputException, JarvisWorkflowException
    {


        Issue issue = ( Issue ) transientVars.get( "issue" );

        List<IssueLink> allInwardIssueLinks = manager.getInwardLinks( issue.getId() );

        Issue destinationIssue;

        for ( IssueLink link : allInwardIssueLinks )
        {
            log.warn( "Found inward issue link {}", link.getSourceObject().getKey() );
            destinationIssue = link.getSourceObject();

            for ( Label label : destinationIssue.getLabels() )
            {
                if ( label.getLabel().equalsIgnoreCase( issue.getStatusObject().getName() ) )
                {
                    if ( !destinationIssue.getStatusObject().getName().equalsIgnoreCase( "CLOSED" )
                            || !destinationIssue.getStatusObject().getName().equalsIgnoreCase( "DONE" ) ||
                            !destinationIssue.getStatusObject().getName().equalsIgnoreCase( "RESOLVED" ) )
                    {
                        throw new JarvisWorkflowException(
                                destinationIssue.getKey() + " must be CLOSED before sending for approval" );
                    }
                }
            }
        }
    }
}