package org.safehaus.service.api;


import java.util.List;

import org.safehaus.dao.entities.Annotation;


/**
 * Created by talas on 10/4/15.
 */
public interface AnnotatorDao
{
    public void insertAnnotation( Annotation annotation );

    public Annotation getAnnotationById( Long id );

    public List<Annotation> getAnnotations();

    public List<Annotation> getAnnotationsByUsername( String username );

    public void deleteAnnotation( Annotation annotation );

    public void updateAnnotation( Annotation annotation );
}
