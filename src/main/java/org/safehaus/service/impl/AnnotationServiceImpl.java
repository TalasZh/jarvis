package org.safehaus.service.impl;


import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.safehaus.dao.entities.Annotation;
import org.safehaus.model.Capture;
import org.safehaus.model.Session;
import org.safehaus.service.api.AnnotatorDao;
import org.safehaus.util.JarvisContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;

import com.google.common.collect.Lists;


/**
 * Created by talas on 10/5/15.
 */
//@Service( "sessionServiceImpl" )
//@WebService( serviceName = "SessionService", endpointInterface = "org.safehaus.service.rest.SessionService" )
public class AnnotationServiceImpl //implements SessionService
{

//    @Autowired
    private AnnotatorDao annotatorDao;


//    @Override
    public Session getSession( final String sessionId )
    {
        return new Session();
    }


//    @Override
    public List<Session> getSessions()
    {
        return Lists.newArrayList();
    }


//    @Override
    public List<Capture> getAllCaptures()
    {
        UserDetails userDetails = JarvisContextHolder.getContext().getUserDetails();
        if ( userDetails == null )
        {
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.UNAUTHORIZED );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }

        List<Capture> result = Lists.newArrayList();
        for ( final Annotation annotation : annotatorDao.getAnnotationsByUsername( userDetails.getUsername() ) )
        {
            Capture capture = new Capture();
            capture.setAnnotator_schema_version( annotation.getAnnotatorSchemaVersion() );
            capture.setCreated( new Date( annotation.getCreated() ) );
            capture.setId( annotation.getId() );
            capture.setOfflineId( annotation.getOfflineId() );
            capture.setQuote( annotation.getQuote() );
            capture.setRanges( annotation.getRanges() );
            capture.setResearchSession( annotation.getResearchSession() );
            capture.setText( annotation.getText() );
            capture.setUri( annotation.getUri() );
            result.add( capture );
        }
        return result;
    }


//    @Override
    public Session startSession( final String issueId )
    {
        return new Session();
    }


//    @Override
    public Session pauseSession( final String sessionId )
    {
        return new Session();
    }


//    @Override
    public Session closeSession( final String sessionId )
    {
        return new Session();
    }


//    @Override
    public Response resolveIssue( final String issueId )
    {
        return Response.ok().build();
    }


//    @Override
    public Response generate( final String issueId )
    {
        return Response.ok().build();
    }


//    @Override
    public Capture updateCapture( final String sessionId, final String captureId, final Capture capture )
    {
        if ( captureId != null )
        {
            Annotation annotation = annotatorDao.getAnnotationById( Long.valueOf( captureId ) );
            annotation.setText( capture.getText() );
            annotatorDao.updateAnnotation( annotation );
        }
        return capture;
    }


//    @Override
    public Response deleteCapture( final String sessionId, final String captureId )
    {
        if ( captureId != null )
        {

            Annotation annotation = annotatorDao.getAnnotationById( Long.valueOf( captureId ) );
            annotatorDao.deleteAnnotation( annotation );
            return Response.ok().build();
        }
        else
        {
            throw new WebApplicationException( Response.Status.NOT_FOUND );
        }
    }


//    @Override
    public Capture saveCapture( final String sessionId, final Capture capture )
    {
        UserDetails userDetails = JarvisContextHolder.getContext().getUserDetails();
        if ( userDetails == null )
        {
            ResponseBuilderImpl builder = new ResponseBuilderImpl();
            builder.status( Response.Status.UNAUTHORIZED );
            Response response = builder.build();
            throw new WebApplicationException( response );
        }

        Annotation annotation = new Annotation();
        annotation.setAuthor( userDetails.getUsername() );
        annotation.setAnnotatorSchemaVersion( capture.getAnnotator_schema_version() );
        annotation.setCreated( new Date().getTime() );
        annotation.setOfflineId( capture.getOfflineId() );
        annotation.setQuote( capture.getQuote() );
        annotation.setRanges( capture.getRanges() );
        annotation.setResearchSession( capture.getResearchSession() );
        annotation.setText( capture.getText() );
        annotation.setUri( capture.getUri() );

        annotatorDao.insertAnnotation( annotation );

        capture.setId( annotation.getId() );
        capture.setCreated( new Date( annotation.getCreated() ) );

        return capture;
    }


//    @Override
    public List<Capture> getCaptures( final String sessionId )
    {
        return Lists.newArrayList();
    }
}
