package org.safehaus.service.impl;


import java.util.List;

import javax.jws.WebService;

import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.jira.JiraIssueChangelog;
import org.safehaus.model.Views;
import org.safehaus.service.rest.JiraChangelogRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonView;


/**
 * Created by ermek on 9/21/15.
 */
@Service( "jiraChangelogManager" )
@WebService( serviceName = "JiraChangelogServiceImpl",
        endpointInterface = "org.safehaus.service.rest.JiraChangelogRestService" )
public class JiraChangelogRestServiceImpl implements JiraChangelogRestService
{
    private static final Logger LOG = LoggerFactory.getLogger( JiraChangelogRestServiceImpl.class );

    @Autowired
    private Dao dao;


    public JiraChangelogRestServiceImpl()
    {
    }


    @Override
    @JsonView( Views.CompleteView.class )
    public List<JiraIssueChangelog> findIssueChangelogByFilter( final String author, final String field,
                                                                final String type )
    {
        String query = "Select j from " + JiraIssueChangelog.class.getSimpleName()
                + " j where j.author =:author and j.field =:field and j.type =:type";
        LOG.info( "Finding IssueChangelog by key author,field,type : {} {} {}", author, field, type );

        List<JiraIssueChangelog> changelogs =
                ( List<JiraIssueChangelog> ) dao.findByQuery( query, "author", author, "field", field, "type", type );
        return changelogs;
    }
}