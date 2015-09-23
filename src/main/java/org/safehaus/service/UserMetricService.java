package org.safehaus.service;

import org.safehaus.analysis.UserMetricInfo;
import java.util.List;

/**
 * Created by kisik on 17.09.2015.
 */
public interface UserMetricService {

    public List<UserMetricInfo> getUserMetricInfoListByDevId(String developerId);

    public UserMetricInfo getUserMetricInfo(String developerId, long timestamp);

    public List<UserMetricInfo> getUserMetricInfoListByMonth(long timestamp);
}
