package org.safehaus.analysis;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by neslihan on 28.08.2015.
 */
@Entity
@Table( name = "user_confluence_metric_info", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "developer_month_info" )})
public class ConfluenceMetricInfo implements Serializable{
    @EmbeddedId
    @Column(name = "developer_month_info")
    private UserMetricInfo.UserMonthInfo developerMonthInfo;

    @Column(name = "confluenceActivityCount")
    private Integer confluenceActivityCount;

    public ConfluenceMetricInfo() {}

    public UserMetricInfo.UserMonthInfo getDeveloperMonthInfo() {
        return developerMonthInfo;
    }

    public Integer getConfluenceActivityCount() {
        return confluenceActivityCount;
    }

    public void setDeveloperMonthInfo(UserMetricInfo.UserMonthInfo developerMonthInfo) {
        this.developerMonthInfo = developerMonthInfo;
    }

    public void setConfluenceActivityCount(Integer confluenceActivityCount) {
        this.confluenceActivityCount = confluenceActivityCount;
    }

    public static class ConfluenceMetricInfoInternal implements Serializable
    {
        private String  developerId;
        private Date metricMonthDate;
        private Integer confluenceActivityCount;

        public ConfluenceMetricInfoInternal(){}

        public String getDeveloperId() {
            return developerId;
        }

        public Integer getConfluenceActivityCount() {
            return confluenceActivityCount;
        }

        public Date getMetricMonthDate() {
            return metricMonthDate;
        }

        public void setDeveloperId(String developerId) {
            this.developerId = developerId;
        }

        public void setConfluenceActivityCount(Integer confluenceActivityCount) {
            this.confluenceActivityCount = confluenceActivityCount;
        }

        public void setMetricMonthDate(Date metricMonthDate) {
            this.metricMonthDate = metricMonthDate;
        }
    }

}
