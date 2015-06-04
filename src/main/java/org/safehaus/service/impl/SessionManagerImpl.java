package org.safehaus.service.impl;


import java.util.List;

import org.safehaus.dao.SessionDao;
import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.Capture;
import org.safehaus.model.JarvisIssue;
import org.safehaus.model.JarvisLink;
import org.safehaus.model.JarvisSessionException;
import org.safehaus.model.PhaseNotFoundException;
import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;
import org.safehaus.model.SessionStatus;
import org.safehaus.service.JiraManager;
import org.safehaus.service.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service( "sessionManager" )
public class SessionManagerImpl extends GenericManagerImpl<Session, Long> implements SessionManager
{
    private static Logger logger = LoggerFactory.getLogger( SessionManagerImpl.class );
    private SessionDao sessionDao;
    //    private CaptureDao captureDao;
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

    //
    //    @Autowired
    //    public void setCaptureDao( final CaptureDao captureDao )
    //    {
    //        this.captureDao = captureDao;
    //    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession( final String sessionId ) throws SessionNotFoundException
    {
        return sessionDao.getSession( sessionId );
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
    public Session startSession( final String id, String username )
            throws JarvisSessionException, PhaseNotFoundException, JiraClientException
    {
        JarvisIssue issue = jiraManager.getIssue( id );
        jiraManager.startIssue( id );

        Session session;
        try
        {
            session = sessionDao.get( issue.getId() );
        }
        catch ( Exception e )
        {
            JarvisLink parentLink = issue.getLink( "Blocks", "OUTBOUND" );

            if ( parentLink == null )
            {
                log.debug( "Could not start session. Parent link not found" );
                throw new JarvisSessionException( "Could not start session. Parent link not found" );
            }

            //            Phase parent = phaseManager.get( parentLink.getId() );
            session = new Session();
            session.setId( issue.getId() );
            session.setIssueId( issue.getId() );
            session.setIssueKey( issue.getKey() );
            session.setUsername( username );
            session.setParentId( jiraManager.getIssue( parentLink.getId().toString() ).getId() );
        }
        session.setStatus( SessionStatus.INPROGRESS );
        sessionDao.saveSession( session );
        return session;
    }


    @Override
    public Session pauseSession( final String sessionId ) throws SessionNotFoundException
    {
        Session session = getSession( sessionId );

        session.setStatus( SessionStatus.PAUSED );
        sessionDao.saveSession( session );
        return session;
    }


    @Override
    public Session closeSession( final String sessionId ) throws SessionNotFoundException
    {
        Session session = getSession( sessionId );

        //TODO: implement close session business processes
        session.setStatus( SessionStatus.CLOSED );
        sessionDao.saveSession( session );
        return session;
    }


    @Override
    public Capture addCapture( final String sessionId, final Capture capture ) throws SessionNotFoundException
    {
        logger.debug( String.format( "%s %s", sessionId, capture ) );
        Session session = getSession( sessionId );
        logger.debug( String.format( "%s %s", session, capture ) );
        session.addCapture( capture );
        saveSession( session );

        return capture;
    }


    @Override
    public Capture updateCapture( final String sessionId, final String captureId, final Capture capture )
            throws SessionNotFoundException
    {
        Session session = getSession( sessionId );
        Capture result = session.updateCapture( new Long( captureId ), capture );
        sessionDao.saveSession( session );
        //        result = captureDao.saveCapture( result );
        logger.debug( "Updated capture:" + result );
        return result;
    }


    @Override
    public List<Session> getSessionsByParentId( final Long parentId )
    {
        return sessionDao.getSessionsByParentId( parentId );
    }
}
