package org.safehaus.webapp.action;


import java.util.List;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.Session;
import org.safehaus.service.SessionManager;
import org.safehaus.util.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class SessionAction extends BaseAction
{
    private SessionManager sessionManager;

    private List<Session> sessions;


    public void setSessionManager( final SessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }


    public List<Session> getSessions()
    {
        return sessions;
    }


    public String list() throws JiraClientException
    {

        log.debug( SecurityContextHolder.getContext().getAuthentication().toString() );
        log.debug( SecurityContextHolder.getContext().getAuthentication().getCredentials().toString() );
        log.debug( SecurityContextHolder.getContext().getAuthentication().getDetails().toString() );
        log.debug( SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString() );
        for ( GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities() )
        {
            log.debug( authority.getAuthority() );
        }

        sessions = sessionManager.getSessions();
        return SUCCESS;
    }
}
