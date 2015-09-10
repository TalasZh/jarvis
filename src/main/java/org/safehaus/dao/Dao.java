package org.safehaus.dao;


import java.util.List;

import javax.persistence.EntityManager;


/**
 * Created by tzhamakeev on 7/2/15.
 */
public interface Dao
{
    EntityManager getEntityManager();

    void closeEntityManager();

    void clearEntityManager();

    void shutDown();

    void insert( Object entity );

    void merge( Object entity );

    void remove( Object entity );

    <T> T findById( Class<T> entityClazz, Object id );

    List<?> findByQuery( String Query );

    List<?> findByQuery( String queryString, String paramater, Object parameterValue );

    <T> int batchInsert( List<T> entities );
}
