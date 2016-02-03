package org.safehaus.service.impl;


import java.util.List;
import java.util.Map;

import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.jira.JiraIssueChangelog;
import org.safehaus.service.api.IssueChangelogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;


/**
 * Created by talas on 10/4/15.
 */
@Service( "issueChangelogDao" )
public class IssueChangelogDaoImpl implements IssueChangelogDao
{
    @Autowired
    Dao dao;


    @Override
    public List<JiraIssueChangelog> getChangelogByUsername( final String username, final int limit )
    {
        String parameter = "author";
        String query =
                String.format( "SELECT ch FROM %s ch WHERE ch.author = :%s", JiraIssueChangelog.class.getSimpleName(),
                        parameter );

        Map<String, Object> params = Maps.newHashMap();
        params.put( parameter, username );

        return dao.findByQueryWithLimit( JiraIssueChangelog.class, query, params, limit );
    }
}
