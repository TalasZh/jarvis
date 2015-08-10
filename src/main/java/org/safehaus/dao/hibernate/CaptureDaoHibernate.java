package org.safehaus.dao.hibernate;


import org.safehaus.dao.CaptureDao;
import org.safehaus.model.Capture;
import org.safehaus.model.CaptureNotFoundException;
import org.springframework.stereotype.Repository;


@Repository( "captureDao" )
public class CaptureDaoHibernate extends GenericDaoHibernate<Capture, Long> implements CaptureDao
{

    /**
     * Constructor that sets the entity to Capture.class.
     */
    public CaptureDaoHibernate()
    {
        super( Capture.class );
    }


    /**
     * {@inheritDoc}
     */
    public Capture saveCapture( Capture capture )
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "capture's id: " + capture.getRemoteId() );
        }
        getSession().saveOrUpdate( capture );
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getSession().flush();
        return capture;
    }


    @Override
    public Capture save( Capture capture )
    {
        return this.saveCapture( capture );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Capture getCapture( String id ) throws CaptureNotFoundException
    {
        try
        {
            return get( new Long( id ) );
        }
        catch ( Exception e )
        {
            throw new CaptureNotFoundException();
        }
    }
}
