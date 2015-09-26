package org.safehaus.service.impl;


import java.util.List;

import org.safehaus.dao.Dao;
import org.safehaus.dao.entities.ConfluenceMetricInfo;
import org.safehaus.service.api.ConfluenceMetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kisik on 17.09.2015.
 */
public class ConfluenceMetricServiceImpl implements ConfluenceMetricService {

    private static final Logger log = LoggerFactory.getLogger(ConfluenceMetricServiceImpl.class);

    @Autowired
    private Dao dao;

    @Override
    public List<ConfluenceMetricInfo> getConfluenceMetricInfoListByDevId(String developerId) {
        log.info("Getting ConfluenceMetricInfo list with developerID: {}", developerId);
        String query =
                "Select j from " + ConfluenceMetricInfo.class.getSimpleName() + " j where j.developerMonthInfo.developerId= " + developerId;
        return (List<ConfluenceMetricInfo>) dao.findByQuery(query);
    }

    @Override
    public ConfluenceMetricInfo getConfluenceMetricInfo(String developerId, long timestamp) {
        log.info( "Getting ConfluenceMetricInfo list");
        String query =
                "Select j from " + ConfluenceMetricInfo.class.getSimpleName() + " j where j.developerMonthInfo.developerId= "
                        + developerId+" and j.developerMonthInfo.metricMonthTimestamp= " + timestamp;
        List<ConfluenceMetricInfo> returnList = (List<ConfluenceMetricInfo>) dao.findByQuery(query);
        if(returnList.size() == 1)
            return returnList.get(0);
        else {
            return null;
        }
    }

    @Override
    public List<ConfluenceMetricInfo> getConfluenceMetricInfoListByMonth(long timestamp) {
        log.info( "Getting ConfluenceMetricInfo list with metricMonthDate: {}", timestamp);
        String query =
                "Select j from " + ConfluenceMetricInfo.class.getSimpleName() + " j where j.developerMonthInfo.metricMonthTimestamp= " + timestamp;
        return (List<ConfluenceMetricInfo>) dao.findByQuery(query);
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
