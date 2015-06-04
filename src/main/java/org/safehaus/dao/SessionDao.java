package org.safehaus.dao;


import java.util.List;

import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;
import org.safehaus.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface SessionDao extends GenericDao<Session, Long>
{
    @Transactional
    List<Session> getSessionsByUsername( String username );

    Session saveSession( Session session );

    Session getSession( String sessionKey )throws SessionNotFoundException;

    List<Session> getSessionsByParentId( Long parentId );
}
