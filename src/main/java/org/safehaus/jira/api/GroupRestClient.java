package org.safehaus.jira.api;


import java.net.URI;

import org.safehaus.jira.impl.Group;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.util.concurrent.Promise;


/**
 * Created by tzhamakeev on 5/20/15.
 */
public interface GroupRestClient
{
    /**
   	 * Retrieves complete information about given group.
   	 *
   	 * @param groupname unique name of the group (usually 2+ characters)
   	 * @return complete information about given group
   	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
   	 */

    Promise<Group> getGroup( String groupname );

    Promise<Group> getGroup(String groupname, Iterable<Expandos> expand);
    //    Promise<Group> getGroup( URI groupUri );

    public static enum Expandos {
        USERS("users");

        private final String value;

        private Expandos(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
