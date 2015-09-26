package org.safehaus.service.impl;


import java.util.List;

import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.UserMetricInfo;
import org.safehaus.service.api.UserMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kisik on 17.09.2015.
 */
@Service
public class UserMetricServiceImpl implements UserMetricService {

    private static final Logger log = LoggerFactory.getLogger(UserMetricServiceImpl.class);
    @Autowired
    private Dao dao;


    @Override
    public List<UserMetricInfo> getUserMetricInfoListByDevId(String developerId) {
        log.info( "Getting UserMetricInfo list with developerID: {}", developerId );
        String query =
                "Select j from " + UserMetricInfo.class.getSimpleName() + " j where j.developerMonthInfo.developerId= " + developerId;
        return (List<UserMetricInfo>) dao.findByQuery(query);
    }

    @Override
    public UserMetricInfo getUserMetricInfo(String developerId, long timestamp) {
        log.info( "Getting UserMetricInfo list");
        String query =
                "Select j from " + UserMetricInfo.class.getSimpleName() + " j where j.developerMonthInfo.developerId= "
                        + developerId+" and j.developerMonthInfo.metricMonthTimestamp= " + timestamp;
        List<UserMetricInfo> returnList = (List<UserMetricInfo>) dao.findByQuery(query);
        if(returnList.size() == 1)
            return returnList.get(0);
        else {
            return null;
        }
    }

    @Override
    public List<UserMetricInfo> getUserMetricInfoListByMonth(long timestamp) {
        log.info( "Getting stashUserMetricInfo list with metricMonthDate: {}", timestamp);
        String query =
                "Select j from " + UserMetricInfo.class.getSimpleName() + " j where j.developerMonthInfo.metricMonthTimestamp= " + timestamp;
        return (List<UserMetricInfo>) dao.findByQuery(query);
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
