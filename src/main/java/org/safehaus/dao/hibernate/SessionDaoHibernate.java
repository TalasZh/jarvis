package org.safehaus.dao.hibernate;


import java.util.List;

import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.safehaus.dao.SessionDao;
import org.safehaus.dao.UserDao;
import org.safehaus.model.Session;
import org.safehaus.model.User;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;


@Repository( "sessionDao" )
public class SessionDaoHibernate extends GenericDaoHibernate<Session, Long> implements SessionDao
{

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
    public List<Session> getSessionsByUsername( String username )
    {
        return getSession().createCriteria( Session.class ).add( Restrictions.eq( "username", username ) ).list();
    }
}
