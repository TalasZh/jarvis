package org.safehaus.service.api;


import java.util.List;

import org.safehaus.dao.entities.ServicePack;


/**
 * Created by talas on 10/3/15.
 */
public interface ServicePackDao
{
    public void insertServicePack( ServicePack servicePack );

    public ServicePack getServicePack( String packId );

    public List<ServicePack> getServicePacks();

    public void updateServicePack( ServicePack servicePack );

    public void deleteServicePack( ServicePack servicePack );
}
