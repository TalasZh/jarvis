package org.safehaus.service.impl;


import java.util.List;

import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.Annotation;
import org.safehaus.service.api.AnnotatorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by talas on 10/4/15.
 */
@Service( "annotatorDao" )
public class AnnotatorDaoImpl implements AnnotatorDao
{
    @Autowired
    private Dao dao;


    @Override
    public void insertAnnotation( final Annotation annotation )
    {
        dao.insert( annotation );
    }


    @Override
    public Annotation getAnnotationById( final Long id )
    {
        return dao.findById( Annotation.class, id );
    }


    @Override
    public List<Annotation> getAnnotations()
    {
        return dao.getAll( Annotation.class );
    }


    @Override
    public void deleteAnnotation( final Annotation annotation )
    {

    }


    @Override
    public void updateAnnotation( final Annotation annotation )
    {

    }
}
