package org.safehaus.model;


import org.codehaus.jackson.annotate.JsonIgnore;


/**
 * Created by tzhamakeev on 5/22/15.
 */
public interface JarvisIssueMixIn
{
    @JsonIgnore
    Phase getPhase();
}
