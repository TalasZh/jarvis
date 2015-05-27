package org.safehaus.service.impl;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.safehaus.dao.SessionDao;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.Session;
import org.safehaus.model.SessionStatus;
import org.safehaus.model.User;
import org.safehaus.service.JiraManager;
import org.safehaus.service.SessionManager;
import org.safehaus.service.SessionService;
import org.safehaus.service.UserExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.apache.commons.lang.StringUtils;


@Service( "sessionManager" )
public class SessionManagerImpl extends GenericManagerImpl<Session, Long> implements SessionManager
{
    private SessionDao sessionDao;
    private JiraManager jiraManager;


    @Override
    @Autowired
    public void setJiraManager( final JiraManager jiraManager )
    {
        this.jiraManager = jiraManager;
    }


    @Override
    @Autowired
    public void setSessionDao( final SessionDao sessionDao )
    {
        this.dao = sessionDao;
        this.sessionDao = sessionDao;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession( final String sessionId )
    {
        return sessionDao.get( new Long( sessionId ) );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Session> getSessions()
    {
        return sessionDao.getAllDistinct();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Session saveSession( final Session session )
    {
        return sessionDao.saveSession( session );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSession( final Session session )
    {
        log.debug( "removing session: " + session );
        sessionDao.remove( session );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSession( final String sessionId )
    {
        log.debug( "removing session: " + sessionId );
        sessionDao.remove( new Long( sessionId ) );
    }


    @Override
    public List<Session> getSessionsByUsername( final String username )
    {
        if ( username == null )
        {
            return sessionDao.getAllDistinct();
        }
        else
        {
            return sessionDao.getSessionsByUsername( username );
        }
    }


    @Override
    public Session startSession( final String sessionId, String username )
    {
        JarvisIssue issue = jiraManager.getIssue( sessionId );

        Long id = issue.getId();
        Session session;
        try
        {
            session = sessionDao.get( id );
        }
        catch ( Exception e )
        {
            session = new Session();
            session.setId( id );
            session.setIssueId( id );
            session.setUsername( username );
        }
        session.setStatus( SessionStatus.INPROGRESS );
        sessionDao.saveSession( session );
        return session;
    }
}
