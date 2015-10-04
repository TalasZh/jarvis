package org.safehaus.dao;


import java.util.List;

import org.safehaus.model.Session;
import org.safehaus.model.SessionNotFoundException;


public interface SessionDao extends GenericDao<Session, Long>
{
    List<Session> getSessionsByUsername( String username );

    Session saveSession( Session session );

    Session getSession( String sessionKey )throws SessionNotFoundException;

    List<Session> getSessionsByParentId( Long parentId );
}
