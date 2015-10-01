package org.safehaus.dao.kundera;


import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.safehaus.Constants;
import org.safehaus.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * Created by talas on 9/9/15.
 */
public class DaoServiceImpl implements Dao
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DaoServiceImpl.class );

    //    @PersistenceUnit( unitName = "cassandra-pu" )
    private EntityManagerFactory emf;


    public EntityManagerFactory getEmf()
    {
        return emf;
    }


    public void setEmf( final EntityManagerFactory emf )
    {
        this.emf = emf;
    }


    @Override
    public <T> List<T> getAll( final Class<T> entityClass )
    {
        List<T> result = Lists.newArrayList();

        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            TypedQuery<T> query =
                    em.createQuery( String.format( "select obj from %s obj", entityClass.getSimpleName() ),
                            entityClass );
            result = query.getResultList();
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error getting all records", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }

        return result;
    }


    @Override
    public void insert( final Object entity )
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            em.persist( entity );
            em.flush();
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error persisting entity", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        finally
        {
            em.close();
        }
    }


    @Override
    public void merge( final Object entity )
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            em.merge( entity );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error merging entities", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        finally
        {
            em.close();
        }
    }


    @Override
    public void remove( final Object entity )
    {
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            em.remove( entity );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error removing entity", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        finally
        {
            em.close();
        }
    }


    @Override
    public <T> T findById( final Class<T> entityClazz, final Object id )
    {
        T result = null;
        EntityManager em = emf.createEntityManager();
        try
        {
            em.getTransaction().begin();
            result = em.find( entityClazz, id );
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error retrieving by id", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        return result;
    }


    @Override
    public <T> List<T> findByQuery( final Class<T> entityClass, final String query,
                                    final Map<String, Object> parameters )
    {
        EntityManager em = emf.createEntityManager();
        List<T> result = Lists.newArrayList();
        try
        {
            em.getTransaction().begin();
            TypedQuery<T> typedQuery = em.createQuery( query, entityClass );

            for ( final Map.Entry<String, Object> entry : parameters.entrySet() )
            {
                typedQuery.setParameter( entry.getKey(), entry.getValue() );
            }
            result = typedQuery.getResultList();
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error executing query", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        return result;
    }


    @Override
    public List<?> findByQuery( final String queryString )
    {
        EntityManager em = emf.createEntityManager();
        List<?> result = Lists.newArrayList();
        try
        {
            em.getTransaction().begin();
            Query query = em.createQuery( queryString );
            result = query.getResultList();
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error executing query", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        return result;
    }


    @Override
    public List<?> findByQuery( final String queryString, final String paramater, final Object parameterValue )
    {
        EntityManager em = emf.createEntityManager();
        List<?> result = Lists.newArrayList();
        try
        {
            em.getTransaction().begin();
            Query query = em.createQuery( queryString );
            query.setParameter( paramater, parameterValue );
            result = query.getResultList();
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error executing query", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        return result;
    }


    @Override
    public List<?> findByQuery( final String queryString, final String parameter1, final Object parameterValue1,
                                final String parameter2, final Object parameterValue2, final String parameter3,
                                final Object parameterValue3 )
    {
        EntityManager em = emf.createEntityManager();
        List<?> result = Lists.newArrayList();
        try
        {
            em.getTransaction().begin();
            Query query = em.createQuery( queryString );
            query.setParameter( parameter1, parameterValue1 );
            query.setParameter( parameter2, parameterValue2 );
            query.setParameter( parameter3, parameterValue3 );
            result = query.getResultList();
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error executing query", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        return result;
    }


    @Override
    public List<?> findByQuery( final String queryString, final String parameter1, final Object parameterValue1,
                                final String parameter2, final Object parameterValue2 )
    {
        EntityManager em = emf.createEntityManager();
        List<?> result = Lists.newArrayList();
        try
        {
            em.getTransaction().begin();
            Query query = em.createQuery( queryString );
            query.setParameter( parameter1, parameterValue1 );
            query.setParameter( parameter2, parameterValue2 );
            result = query.getResultList();
            em.getTransaction().commit();
        }
        catch ( Exception ex )
        {
            LOGGER.error( "Error executing query", ex );
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
        }
        return result;
    }


    @Override
    public <T> int batchInsert( final List<T> entities )
    {
        int totalPersisted = 0;
        EntityManager em = emf.createEntityManager();
        try
        {
            if ( entities.size() > 0 )
            {
                em.getTransaction().begin();
                int counter = 0;
                for ( final T entity : entities )
                {
                    em.persist( entity );
                    if ( ++counter == Constants.BATCH_SIZE )
                    {
                        counter = 0;
                        em.flush();
                        totalPersisted += counter;
                        //                        em.clear();
                    }
                }
                if ( counter > 0 )
                {
                    em.clear();
                    totalPersisted += counter;
                    //                    em.clear();
                }
                em.getTransaction().commit();
            }
        }
        catch ( Exception ex )
        {
            if ( em.getTransaction().isActive() )
            {
                em.getTransaction().rollback();
            }
            LOGGER.error( "Error batch inserting data.", ex );
        }
        finally
        {
            em.close();
        }
        return totalPersisted;
    }
}
