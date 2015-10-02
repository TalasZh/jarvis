package org.safehaus.dao.hibernate;


import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.safehaus.dao.SessionDao;
import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.validator.GenericValidator;


@Repository( "sessionDao" )
public class SessionDaoHibernate extends GenericDaoHibernate<Session, Long> implements SessionDao
{
    private static Logger logger = LoggerFactory.getLogger( SessionDaoHibernate.class );


    /**
     * Constructor that sets the entity to Session.class.
     */
    public SessionDaoHibernate()
    {
        super( Session.class );
    }


    /**
     * {@inheritDoc}
     */
    public Session saveSession( Session session )
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "session's id: " + session.getId() );
        }
        getSession().saveOrUpdate( session );
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getSession().flush();
        return session;
    }


    /**
     * Overridden simply to call the saveUser method. This is happening because saveUser flushes the session and
     * saveObject of BaseDaoHibernate does not.
     *
     * @param session the session to save
     *
     * @return the modified session (with a primary key set if they're new)
     */
    @Override
    public Session save( Session session )
    {
        return this.saveSession( session );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<Session> getSessionsByUsername( String username )
    {
        return getSession().createCriteria( Session.class ).add( Restrictions.eq( "username", username ) ).list();
    }


    @Override
    public List<Session> getSessionsByParentId( final Long parentId )
    {
        return getSession().createCriteria( Session.class ).add( Restrictions.eq( "parentId", parentId ) ).list();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Session getSession( String id ) throws SessionNotFoundException
    {
        if ( GenericValidator.isLong( id ) )
        {
            try
            {
                return get( new Long( id ) );
            }
            catch ( Exception e )
            {
                throw new SessionNotFoundException();
            }
        }

        List<Session> sessions =
                getSession().createCriteria( Session.class ).add( Restrictions.eq( "issueKey", id ) ).list();

        if ( !sessions.isEmpty() )
        {
            return sessions.get( 0 );
        }
        else
        {
            logger.debug( "Session lookup by issue key failed." );
            throw new SessionNotFoundException();
        }
    }
}
