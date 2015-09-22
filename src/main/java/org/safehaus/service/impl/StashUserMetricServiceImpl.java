package org.safehaus.service.impl;


import java.util.List;

import org.safehaus.analysis.StashUserMetricInfo;
import org.safehaus.dao.Dao;
import org.safehaus.service.StashUserMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by kisik on 11.09.2015.
 */
@Service
public class StashUserMetricServiceImpl implements StashUserMetricService
{

    private static final Logger log = LoggerFactory.getLogger( StashUserMetricServiceImpl.class );

    @Autowired
    private Dao dao;


    @Override
    public List<StashUserMetricInfo> getStashUserMetricInfoListByDevId( String developerId )
    {

        log.info( "Getting stashUserMetricInfo list with developerID: {}", developerId );
        String query = "Select j from " + StashUserMetricInfo.class.getSimpleName()
                + " j where j.developerMonthInfo.developerId= " + developerId;
        return ( List<StashUserMetricInfo> ) dao.findByQuery( query );
    }


    @Override
    public StashUserMetricInfo getStashUserMetricInfo( String developerId, long timestamp )
    {

        log.info( "Getting stashUserMetricInfo list" );
        String query = "Select j from " + StashUserMetricInfo.class.getSimpleName()
                + " j where j.developerMonthInfo.developerId= " + developerId
                + " and j.developerMonthInfo.metricMonthTimestamp= " + timestamp;
        List<StashUserMetricInfo> returnList = ( List<StashUserMetricInfo> ) dao.findByQuery( query );
        if ( returnList.size() == 1 )
        {
            return returnList.get( 0 );
        }
        else
        {
            return null;
        }
    }


    @Override
    public List<StashUserMetricInfo> getStashUserMetricInfoListByMonth( long timestamp )
    {
        log.info( "Getting stashUserMetricInfo list with metricMonthDate: {}", timestamp );
        String query = "Select j from " + StashUserMetricInfo.class.getSimpleName()
                + " j where j.developerMonthInfo.metricMonthTimestamp= " + timestamp;
        return ( List<StashUserMetricInfo> ) dao.findByQuery( query );
    }


    public Dao getDao()
    {
        return dao;
    }


    public void setDao( Dao dao )
    {
        this.dao = dao;
    }
}
