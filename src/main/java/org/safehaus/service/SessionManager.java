package org.safehaus.service;


import java.util.List;

import org.safehaus.dao.SessionDao;
import org.safehaus.model.Session;
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
    Session getSession( String sessionId );


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

    Session startSession( String sessionId, String username );
}
