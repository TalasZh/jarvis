package org.safehaus.dao;


import java.util.List;
import java.util.Map;


/**
 * Created by tzhamakeev on 7/2/15.
 */
public interface Dao
{
    <T> List<T> getAll( Class<T> entityClass );

    void insert( Object entity );

    void merge( Object entity );

    void remove( Object entity );

    <T> T findById( Class<T> entityClazz, Object id );

    <T> List<T> findByQuery( Class<T> entityClass, String query, Map<String, Object> parameters );

    <T> List<T> findByQueryWithLimit( Class<T> entityClass, String query, Map<String, Object> parameters, int limit );

    List<?> findByQuery( String Query );

    List<?> findByQuery( String queryString, String paramater, Object parameterValue );

    List<?> findByQuery( String queryString, String parameter1, Object parameterValue1, String parameter2,
                         Object parameterValue2, String parameter3, Object parameterValue3 );

    List<?> findByQuery( String queryString, String parameter1, Object parameterValue1, String parameter2,
                         Object parameterValue2 );

    <T> int batchInsert( List<T> entities );
}
