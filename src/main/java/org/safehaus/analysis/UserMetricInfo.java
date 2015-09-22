package org.safehaus.analysis;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

/**
 * Created by neslihan on 17.08.2015.
 */
@Entity
@Table( name = "user_jira_metric_info", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "developer_month_info" )})
public class UserMetricInfo implements Serializable{

    @Embeddable()
    public static class UserMonthInfo implements Serializable{

        @Column(name = "developer_id")
        private String developerId;

        @Column(name = "metric_month_timestamp")
        private long metricMonthTimestamp;

        public UserMonthInfo(String developerId, long metricMonthTimestamp)
        {
            this.developerId = developerId;
            this.metricMonthTimestamp = metricMonthTimestamp;
        }

        public UserMonthInfo(){}

        public String getDeveloperId() {
            return developerId;
        }

        public void setDeveloperId(String developerId) {
            this.developerId = developerId;
        }

        public long getMetricMonthTimestamp() {
            return metricMonthTimestamp;
        }

        public void setMetricMonthTimestamp(long metricMonthTimestamp) {
            this.metricMonthTimestamp = metricMonthTimestamp;
        }
    }

    @EmbeddedId
    @Column(name = "developer_month_info")
    private UserMonthInfo  developerMonthInfo;

    @Column(name = "jira_productivity")
    private Double jiraProductivity;


    public UserMetricInfo(){}

    public Double getJiraProductivity() {
        return jiraProductivity;
    }

    public void setJiraProductivity(Double jiraProductivity) {
        this.jiraProductivity = jiraProductivity;
    }

    public UserMonthInfo getDeveloperMonthInfo() {
        return developerMonthInfo;
    }

    public void setDeveloperMonthInfo(UserMonthInfo developerMonthInfo) {
        this.developerMonthInfo = developerMonthInfo;
    }

    //Work-around for hibernate changing the table column names
    // and cassandra-streaming not being compatible with it.
    public static class UserMetricInfoInternal implements Serializable
    {
        private String  developerId;

        private Double jiraProductivity;

        private long metricMonthTimestamp;

        public UserMetricInfoInternal(){}

        public String getDeveloperId() {
            return developerId;
        }

        public Double getJiraProductivity() {
            return jiraProductivity;
        }

        public long getMetricMonthTimestamp()
        {
            return metricMonthTimestamp;
        }


        public void setMetricMonthTimestamp( final long metricMonthTimestamp )
        {
            this.metricMonthTimestamp = metricMonthTimestamp;
        }

        public void setJiraProductivity(Double jiraProductivity) {
            this.jiraProductivity = jiraProductivity;
        }

        public void setDeveloperId(String developerId) {
            this.developerId = developerId;
        }
    }
}
