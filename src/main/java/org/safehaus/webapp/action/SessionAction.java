package org.safehaus.webapp.action;


import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.safehaus.exceptions.JiraClientException;
import org.safehaus.model.DataFormat;
import org.safehaus.model.DocumentInfo;
import org.safehaus.model.Session;
import org.safehaus.service.DataStore;
import org.safehaus.service.SessionManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Created by tzhamakeev on 6/3/15.
 */
public class SessionAction extends BaseAction
{
    private SessionManager sessionManager;

    private DataStore dataStore;

    private List<Session> sessions;


    public void setSessionManager( final SessionManager sessionManager )
    {
        this.sessionManager = sessionManager;
    }


    public void setDataStore( final DataStore dataStore )
    {
        this.dataStore = dataStore;
    }


    public List<Session> getSessions()
    {
        return sessions;
    }


    public String list() throws JiraClientException
    {

        log.debug( SecurityContextHolder.getContext().getAuthentication().toString() );
        for ( GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities() )
        {
            log.debug( authority.getAuthority() );
        }

        sessions = sessionManager.getSessions();

        try
        {
            DocumentInfo document = new DocumentInfo();
            String id = UUID.randomUUID().toString();
            document.setKey( id );
            document.setDocumentName( "Test Document:" + id );
            document.setOwnerName( "Timur" );
            document.setDataFormat( DataFormat.DOC );
            document.setData( id.getBytes() );
            document.setSize( id.length() );
            document.setUplodedDate( new Date() );
            dataStore.insertDocument( document );
            DocumentInfo documentInfo=dataStore.findDocument( id );
            log.debug( "Last created document: "+ documentInfo );
            List<DocumentInfo> docs = dataStore.findDocumentByOwnerName( "Timur" );

            log.debug("All documents:");
            for (DocumentInfo d: docs) {
                log.debug( documentInfo );
            }
            dataStore.removeDocument( documentInfo );

        }
        catch ( Exception e )
        {
            log.error( e.getMessage(), e );
        }
        return SUCCESS;
    }
}
