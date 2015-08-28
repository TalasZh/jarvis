package org.safehaus.dao.kundera;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

import org.safehaus.Constants;
import org.safehaus.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impetus.client.cassandra.common.CassandraConstants;
import com.impetus.kundera.client.Client;


/**
 * Created by tzhamakeev on 7/2/15.
 */
public class DaoImpl implements Dao
{
    /**
     * logger used for logging statement.
     */
    private static final Logger log = LoggerFactory.getLogger( DaoImpl.class );

    @PersistenceContext( unitName = "cassandra-pu", type = PersistenceContextType.EXTENDED )
    //            properties = {
    //                    @PersistenceProperty( name = CassandraConstants.CQL_VERSION, value = CassandraConstants
    //                            .CQL_VERSION_3_0 )
    //            } )
    private EntityManager em;


    public DaoImpl()
    {
    }


    @Override
    public void insert( Object entity )
    {
        em.persist( entity );
        em.flush();
        em.clear();
    }


    @Override
    public void merge( Object entity )
    {
        em.merge( entity );
        em.flush();
        em.clear();
    }


    @Override
    public void remove( Object entity )
    {
        em.remove( entity );
        em.flush();
        em.clear();
    }


    @Override
    public <T> T findById( Class<T> entityClazz, Object id )
    {
        T results = em.find( entityClazz, id );
        return results;
    }


    @Override
    public List<?> findByQuery( String queryString )
    {
        log.info( queryString );
        Query query = em.createQuery( queryString );
        List<?> resultList = query.getResultList();
        return resultList;
    }


    @Override
    public List<?> findByQuery( String queryString, String paramater, Object parameterValue )
    {
        Query query = em.createQuery( queryString );
        query.setParameter( paramater, parameterValue );
        log.info( queryString );
        List<?> resultList = query.getResultList();
        return resultList;
    }


    @Override
    public <T> void batchInsert( final List<T> entities )
    {
        int counter = 0;
        for ( final T entity : entities )
        {
            em.persist( entity );
            if ( ++counter == Constants.BATCH_SIZE )
            {
                counter = 0;
                em.flush();
                em.clear();
            }
        }
        em.flush();
        em.clear();
    }


    @Override
    public EntityManager getEntityManager()
    {
        return em;
    }


    @Override
    public void closeEntityManager()
    {
        if ( em != null )
        {
            em.close();
        }
    }


    @Override
    public void clearEntityManager()
    {
        if ( em != null )
        {
            em.clear();
        }
    }


    @Override
    public void shutDown()
    {
        if ( em != null )
        {
            em.close();
        }
    }
}
