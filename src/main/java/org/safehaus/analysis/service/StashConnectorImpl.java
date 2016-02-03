package org.safehaus.analysis.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.safehaus.stash.client.StashManager;
import org.safehaus.stash.client.StashManagerException;
import org.safehaus.stash.client.StashManagerImpl;

/**
 * Created by kisik on 29.07.2015.
 */
public class StashConnectorImpl implements StashConnector {
    private static final Log log = LogFactory.getLog(StashConnectorImpl.class);
    private String stashURL;
    private String stashUserName;
    private String stashPass;

    StashConnectorImpl(String stashURL, String stashUserName, String stashPass) {
        this.stashURL = stashURL;
        this.stashUserName = stashUserName;
        this.stashPass = stashPass;
    }

    public StashManager stashConnect() throws StashManagerException {
        log.info("stashConnect()");
        StashManager stashMan = null;
        stashMan = new StashManagerImpl(stashURL, stashUserName, stashPass);
        return stashMan;
    }
}
