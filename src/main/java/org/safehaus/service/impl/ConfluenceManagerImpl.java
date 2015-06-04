package org.safehaus.service.impl;


import java.util.List;

import org.safehaus.model.JarvisSpace;
import org.safehaus.service.ConfluenceManager;
import org.springframework.stereotype.Service;


/**
 * Created by tzhamakeev on 5/29/15.
 */
@Service( "confluenceManager" )
public class ConfluenceManagerImpl implements ConfluenceManager
{
    @Override
    public List<JarvisSpace> geSpaces()
    {
        return null;
    }
}
