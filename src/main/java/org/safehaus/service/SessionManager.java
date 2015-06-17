package org.safehaus.service;


import java.util.List;

import org.safehaus.dao.SessionDao;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.jira.JiraManager;
import org.safehaus.model.Capture;
import org.safehaus.model.JarvisSessionException;
import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;


public interface SessionManager extends GenericManager<Session, Long>
{
    @Autowired
    void setJiraManager( JiraManager jiraManager );

    /**
     * Convenience method for testing - allows you to mock the DAO and set it on an interface.
     *
     * @param sessionDao the SessionDao implementation to use
     */
    void setSessionDao( SessionDao sessionDao );

    /**
     * Retrieves a session by sessionId.  An exception is thrown if session not found
     *
     * @param sessionId the identifier for the session
     *
     * @return Session
     */
    Session getSession( String sessionId ) throws SessionNotFoundException;


    /**
     * Retrieves a list of all sessions.
     *
     * @return List
     */
    List<Session> getSessions();

    /**
     * Retrieves a list of all user's sessions.
     *
     * @return List
     */
    List<Session> getSessionsByUsername( String username );

    /**
     * Saves a session's information.
     *
     * @param session the session's information
     *
     * @return session the updated session object
     */
    Session saveSession( Session session );

    /**
     * Removes a session from the database
     *
     * @param session the session to remove
     */
    void removeSession( Session session );

    /**
     * Removes a session from the database by their sessionId
     *
     * @param sessionId the session's id
     */
    void removeSession( String sessionId );

    Session startSession( String sessionId, String username )
            throws JarvisSessionException, JiraClientException;

    Session pauseSession( String sessionId ) throws SessionNotFoundException;

    Session closeSession( String sessionId ) throws SessionNotFoundException;

    Capture updateCapture( String sessionId, String captureId, Capture capture ) throws SessionNotFoundException;

    Capture addCapture( String sessionId, Capture capture ) throws SessionNotFoundException;

    List<Session> getSessionsByParentId( Long parentId );
}
