package org.safehaus.analysis;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;
import org.safehaus.analysis.UserMetricInfo.UserMonthInfo;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by neslihan on 25.08.2015.
 */
@Entity
@Table( name = "user_stash_metric_info", schema = "jarvis@cassandra-pu" )
@IndexCollection( columns = {
        @Index( name = "developer_month_info" )})
public class StashUserMetricInfo implements Serializable{
    @EmbeddedId
    @Column(name = "developer_month_info")
    private UserMonthInfo  developerMonthInfo;

    @Column(name = "stashCommitCnt")
    private Integer stashCommitCnt;

    public StashUserMetricInfo(){};

    public Integer getStashCommitCnt() {
        return stashCommitCnt;
    }

    public UserMonthInfo getDeveloperMonthInfo() {
        return developerMonthInfo;
    }

    public void setDeveloperMonthInfo(UserMonthInfo developerMonthInfo) {
        this.developerMonthInfo = developerMonthInfo;
    }

    public void setStashCommitCnt(Integer stashCommitCnt) {
        this.stashCommitCnt = stashCommitCnt;
    }

    public static class StashUserMetricInfoInternal implements Serializable
    {
        private String  developerId;

        private Integer stashCommitCnt;

        private Date metricMonthDate;

        public StashUserMetricInfoInternal(){}

        public String getDeveloperId() {
            return developerId;
        }

        public Integer getStashCommitCnt() {
            return stashCommitCnt;
        }

        public Date getMetricMonthDate() {
            return metricMonthDate;
        }

        public void setDeveloperId(String developerId) {
            this.developerId = developerId;
        }

        public void setStashCommitCnt(Integer jiraCommitCnt) {
            this.stashCommitCnt = jiraCommitCnt;
        }

        public void setMetricMonthDate(Date metricMonthDate) {
            this.metricMonthDate = metricMonthDate;
        }
    }


}
