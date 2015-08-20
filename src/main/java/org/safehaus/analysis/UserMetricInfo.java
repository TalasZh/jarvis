package org.safehaus.analysis;

import java.io.Serializable;
import java.util.Date;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by neslihan on 17.08.2015.
 */
@Entity
@Table( name = "user_jira_metric_info", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "developer_id" )})
public class UserMetricInfo implements Serializable{

    @Id
    @Column(name = "developer_id")
    private String  developerId;

    @Column(name = "jira_productivity")
    private Double jiraProductivity;

    @Column(name = "metric_month_date")
    private Date metricMonthDate;

    public UserMetricInfo(){}

    public String getDeveloperId() {
        return developerId;
    }

    public Double getJiraProductivity() {
        return jiraProductivity;
    }

    public Date getMetricMonthDate()
    {
        return metricMonthDate;
    }


    public void setMetricMonthDate( final Date metricMonthDate )
    {
        this.metricMonthDate = metricMonthDate;
    }

    public void setJiraProductivity(Double jiraProductivity) {
        this.jiraProductivity = jiraProductivity;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

}
