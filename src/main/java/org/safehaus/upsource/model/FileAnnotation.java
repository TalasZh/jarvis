package org.safehaus.upsource.model;


import java.util.Set;

import com.google.common.base.Objects;


public class FileAnnotation
{
    private Set<FileAnnotationSection> retrospective;
    private Set<FileAnnotationSection> perspective;


    public Set<FileAnnotationSection> getRetrospective()
    {
        return retrospective;
    }


    public Set<FileAnnotationSection> getPerspective()
    {
        return perspective;
    }


    @Override
    public String toString()
    {
        return Objects.toStringHelper( this ).add( "retrospective", retrospective ).add( "perspective", perspective )
                      .toString();
    }
}
