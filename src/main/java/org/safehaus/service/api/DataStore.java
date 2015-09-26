package org.safehaus.service.api;


import java.util.List;

import org.safehaus.model.DocumentInfo;


/**
 * Created by tzhamakeev on 7/2/15.
 */
public interface DataStore
{
    DocumentInfo findDocumentByDocumentId( int documentId );

    DocumentInfo findDocumentByName( String documentName );

    List<DocumentInfo> findDocumentByOwnerName( String ownerName );

    void insertDocument( DocumentInfo documentInfo );

    DocumentInfo findDocument( Object documentId );

    void removeDocument( DocumentInfo documentInfo );

}
