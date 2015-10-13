package org.safehaus.analysis.service;


import org.safehaus.stash.client.StashClient;
import org.safehaus.stash.client.StashManagerException;

/**
 * Created by kisik on 30.07.2015.
 */
public interface StashConnector {
    StashClient stashConnect() throws StashManagerException;
}
