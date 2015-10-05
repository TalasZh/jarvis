package org.safehaus.service.rest;


import java.util.List;

import javax.jws.WebService;
import javax.websocket.server.PathParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.xml.ws.Response;

import org.safehaus.dao.entities.Annotation;


/**
 * Created by talas on 10/5/15.
 */
@WebService
@Path( "/annotation" )
public interface AnnotationService
{
    @POST
    public void saveAnnotation( Annotation annotation );

    @GET
    public List<Annotation> getAnnotations();

    @PUT
    @Path( "{annotationId}" )
    public void updateAnnotation( @PathParam( "annotationId" ) String annotationId );

    @DELETE
    @Path( "{annotationId}" )
    public Response deleteAnnotation( @PathParam( "annotationId" ) String annotationId );
}
