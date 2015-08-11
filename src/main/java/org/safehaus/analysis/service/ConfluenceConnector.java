package org.safehaus.analysis.service;

import org.safehaus.confluence.client.ConfluenceManager;
import org.safehaus.confluence.client.ConfluenceManagerException;

/**
 * Created by kisik on 06.08.2015.
 */
public interface ConfluenceConnector {
    ConfluenceManager confluenceConnect() throws ConfluenceManagerException;
}
