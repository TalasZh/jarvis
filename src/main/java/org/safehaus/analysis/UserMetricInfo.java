package org.safehaus.analysis;

import java.io.Serializable;
import java.util.Date;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.*;

/**
 * Created by neslihan on 17.08.2015.
 */
@Entity
@Table( name = "user_jira_metric_info", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "developer_month_info" )})
public class UserMetricInfo implements Serializable{

    @Embeddable()
    public static class UserMonthInfo{

        @Column(name = "developer_id")
        private String developerId;

        @Column(name = "metric_month_date")
        private Date metricMonthDate;

        public UserMonthInfo(String developerId, Date metricMonthDate)
        {
            this.developerId = developerId;
            this.metricMonthDate = metricMonthDate;
        }

        public String getDeveloperId() {
            return developerId;
        }

        public void setDeveloperId(String developerId) {
            this.developerId = developerId;
        }

        public Date getMetricMonthDate() {
            return metricMonthDate;
        }

        public void setMetricMonthDate(Date metricMonthDate) {
            this.metricMonthDate = metricMonthDate;
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
}
