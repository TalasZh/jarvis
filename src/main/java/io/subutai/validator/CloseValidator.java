package io.subutai.validator;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.link.IssueLink;
import com.atlassian.jira.issue.link.IssueLinkManager;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Validator;

import io.subutai.exception.JarvisWorkflowException;


public class CloseValidator implements Validator
{
    private static final Logger log = LoggerFactory.getLogger( CloseValidator.class );
    private final IssueLinkManager manager;


    public CloseValidator( final IssueLinkManager manager )
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

            if ( !destinationIssue.getStatusObject().getName().equalsIgnoreCase( "CLOSED" )
                    || !destinationIssue.getStatusObject().getName().equalsIgnoreCase( "DONE" )
                    ||!destinationIssue.getStatusObject().getName().equalsIgnoreCase( "RESOLVED" ))
            {
                throw new JarvisWorkflowException(
                        destinationIssue.getKey() + " must be CLOSED before CLOSING ISSUE" );
            }
        }
    }
}
