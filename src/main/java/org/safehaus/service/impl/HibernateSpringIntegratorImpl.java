package org.safehaus.service.impl;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
import org.safehaus.service.HibernateSpringIntegrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


public class HibernateSpringIntegratorImpl implements HibernateSpringIntegrator, InitializingBean
{

    private static final Logger log = LoggerFactory.getLogger( HibernateSpringIntegratorImpl.class );


    //    @Autowired(required = false)
    //    private List<HibernateEventListener> hibernateEventListeners;

//    @Resource
    private SessionFactory sessionFactory;

//    @Autowired
//    @Qualifier("ApplicationEntityManager")
    private EntityManager applicationEntityManager;

    //    @Autowired
    //    private HibernateSpringIntegratorRegistry hibernateSpringIntegratorRegistry;


    @Override
    public void afterPropertiesSet() throws Exception
    {
        registerListeners();
    }


    public void registerListeners()
    {
        System.out.println( "*********************************************" );
        log.debug( "Registering Spring managed HibernateEventListeners" );

        log.info( "Session factory: " + sessionFactory );


        log.info( "Entity manager factory: " + applicationEntityManager );

        //        sessionFactory.
        //
        //        EventListenerRegistry listenerRegistry = ((SessionFactoryImpl ) entityManagerFactory
        //                .getSessionFactory()).getServiceRegistry().getService(
        //                EventListenerRegistry.class);
        //        List<HibernateEventListener> eventListeners = hibernateSpringIntegratorRegistry
        //                .getHibernateEventListeners();
        //        for (HibernateEventListener hel : eventListeners) {
        //            log.debug("Registering: {}", hel.getClass());
        //            if (PreInsertEventListener.class.isAssignableFrom(hel.getClass())) {
        //                listenerRegistry.appendListeners( EventType.PRE_INSERT,
        //                        (PreInsertEventListener) hel);
        //            }
        //            if (PreUpdateEventListener.class.isAssignableFrom(hel.getClass())) {
        //                listenerRegistry.appendListeners(EventType.PRE_UPDATE,
        //                        (PreUpdateEventListener) hel);
        //            }
        //            if (PreDeleteEventListener.class.isAssignableFrom(hel.getClass())) {
        //                listenerRegistry.appendListeners(EventType.PRE_DELETE,
        //                        (PreDeleteEventListener) hel);
        //            }
        //            if (PostInsertEventListener.class.isAssignableFrom(hel.getClass())) {
        //                listenerRegistry.appendListeners(EventType.POST_INSERT,
        //                        (PostInsertEventListener) hel);
        //            }
        //            if (PostUpdateEventListener.class.isAssignableFrom(hel.getClass())) {
        //                listenerRegistry.appendListeners(EventType.POST_UPDATE,
        //                        (PostUpdateEventListener) hel);
        //            }
        //            if (PostDeleteEventListener.class.isAssignableFrom(hel.getClass())) {
        //                listenerRegistry.appendListeners(EventType.POST_DELETE,
        //                        (PostDeleteEventListener) hel);
        //            }
        //            // Currently we do not need other types of eventListeners. Else this method needs to be extended.
        //        }
    }
}