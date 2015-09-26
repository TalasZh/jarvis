package org.safehaus.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.safehaus.dao.Dao;
import org.safehaus.model.DocumentInfo;
import org.safehaus.service.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by tzhamakeev on 7/2/15.
 */
public class DataStoreImpl implements DataStore
{
    /**
     * logger used for logging statement.
     */
    private static final Logger log = LoggerFactory.getLogger( DataStoreImpl.class );

    private Dao dao;


    public DataStoreImpl()
    {
    }


    public Dao getDao()
    {
        return dao;
    }


    public void setDao( Dao dao )
    {
        this.dao = dao;
    }


    @Override
    public void insertDocument( DocumentInfo documentInfo )
    {
        dao.insert( documentInfo );
        log.info( "Document {} information successfully inserted.", documentInfo.getDocumentName() );
    }


    @Override
    public DocumentInfo findDocumentByName( String documentName )
    {
        DocumentInfo documentInfo = null;
        String query =
                "Select d from " + DocumentInfo.class.getSimpleName() + " d where d.documentName = " + documentName;
        List<DocumentInfo> documents = ( List<DocumentInfo> ) dao.findByQuery( query );
        if ( !documents.isEmpty() && documents.get( 0 ) != null )
        {
            documentInfo = documents.get( 0 );
        }
        return documentInfo;
    }


    @Override
    public DocumentInfo findDocument( Object documentId )
    {
        log.info( "Finding document by id {} .", documentId );
        return dao.findById( DocumentInfo.class, documentId );
    }


    @Override
    public List<DocumentInfo> findDocumentByOwnerName( String ownerName )
    {
        List<DocumentInfo> documents = new ArrayList<DocumentInfo>();
        log.info( "Finding document by owner name {} .", ownerName );
        String query = "Select d from " + DocumentInfo.class.getSimpleName() + " d where d.ownerName =  " + ownerName;
        documents = ( List<DocumentInfo> ) dao.findByQuery( query );
        return documents;
    }

    @Override
    public void removeDocument( DocumentInfo documentInfo )
    {
        dao.remove( documentInfo );
        log.info( "Document successfully removed" );
    }

    @Override
    public DocumentInfo findDocumentByDocumentId( int documentId )
    {
        log.info( "Finding document by id." );
        DocumentInfo document = dao.findById( DocumentInfo.class, documentId );
        return document;
    }
}
