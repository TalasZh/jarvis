package org.safehaus.service.api;


import java.util.List;

import org.safehaus.dao.entities.jira.JiraIssueChangelog;


/**
 * Created by talas on 10/4/15.
 */
public interface IssueChangelogDao
{
    List<JiraIssueChangelog> getChangelogByUsername( String username, int limit );
}
