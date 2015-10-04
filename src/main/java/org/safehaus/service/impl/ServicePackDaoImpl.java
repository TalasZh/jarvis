package org.safehaus.service.impl;


import java.util.List;

import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.ServicePack;
import org.safehaus.service.api.ServicePackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by talas on 10/3/15.
 */
@Service( "servicePackDao" )
public class ServicePackDaoImpl implements ServicePackDao
{
    @Autowired
    private Dao dao;


    @Override
    public void insertServicePack( ServicePack servicePack )
    {
        dao.insert( servicePack );
    }


    @Override
    public ServicePack getServicePack( final String packId )
    {
        return dao.findById( ServicePack.class, packId );
    }


    @Override
    public List<ServicePack> getServicePacks()
    {
        return dao.getAll( ServicePack.class );
    }


    @Override
    public void updateServicePack( final ServicePack servicePack )
    {
        dao.merge( servicePack );
    }


    @Override
    public void deleteServicePack( final ServicePack servicePack )
    {
        dao.remove( servicePack );
    }
}
