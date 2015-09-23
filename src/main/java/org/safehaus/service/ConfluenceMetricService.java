package org.safehaus.service;

import org.safehaus.analysis.ConfluenceMetricInfo;

import java.util.List;

/**
 * Created by kisik on 17.09.2015.
 */
public interface ConfluenceMetricService {
    public List<ConfluenceMetricInfo> getConfluenceMetricInfoListByDevId(String developerId);

    public ConfluenceMetricInfo getConfluenceMetricInfo(String developerId, long timestamp);

    public List<ConfluenceMetricInfo> getConfluenceMetricInfoListByMonth(long timestamp);
}
