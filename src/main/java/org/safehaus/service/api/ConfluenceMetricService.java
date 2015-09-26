package org.safehaus.service.api;


import java.util.List;

import org.safehaus.dao.entities.ConfluenceMetricInfo;

/**
 * Created by kisik on 17.09.2015.
 */
public interface ConfluenceMetricService {
    public List<ConfluenceMetricInfo> getConfluenceMetricInfoListByDevId(String developerId);

    public ConfluenceMetricInfo getConfluenceMetricInfo(String developerId, long timestamp);

    public List<ConfluenceMetricInfo> getConfluenceMetricInfoListByMonth(long timestamp);
}
