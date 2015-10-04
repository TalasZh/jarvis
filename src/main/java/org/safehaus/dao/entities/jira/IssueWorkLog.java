package org.safehaus.dao.entities.jira;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


/**
 * Created by talas on 10/4/15.
 */
@Entity
@Table( name = "jira_issue_work_log", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "author" ), @Index( name = "updateAuthor" ), @Index( name = "createDate" )
} )
public class IssueWorkLog implements Serializable
{
    @Id
    @Column( name = "work_log_id" )
    private String workLogId;

    @Column( name = "comment" )
    private String comment;

    @Column( name = "author" )
    private String author;

    @Column( name = "create_date" )
    @OrderBy( "createDate DESC" )
    private Long createDate;

    @Column( name = "time_spent_sec" )
    private int timeSpentSeconds;

    @Column( name = "update_author" )
    private String updateAuthor;

    @Column( name = "update_date" )
    private Long updateDate;


    public IssueWorkLog()
    {
    }


    public IssueWorkLog( final String workLogId, final String comment, final String author, final Long createDate,
                         final int timeSpentSeconds, final String updateAuthor, final Long updateDate )
    {
        this.workLogId = workLogId;
        this.comment = comment;
        this.author = author;
        this.createDate = createDate;
        this.timeSpentSeconds = timeSpentSeconds;
        this.updateAuthor = updateAuthor;
        this.updateDate = updateDate;
    }


    public String getWorkLogId()
    {
        return workLogId;
    }


    public void setWorkLogId( final String workLogId )
    {
        this.workLogId = workLogId;
    }


    public String getComment()
    {
        return comment;
    }


    public void setComment( final String comment )
    {
        this.comment = comment;
    }


    public String getAuthor()
    {
        return author;
    }


    public void setAuthor( final String author )
    {
        this.author = author;
    }


    public Long getCreateDate()
    {
        return createDate;
    }


    public void setCreateDate( final Long createDate )
    {
        this.createDate = createDate;
    }


    public int getTimeSpentSeconds()
    {
        return timeSpentSeconds;
    }


    public void setTimeSpentSeconds( final int timeSpentSeconds )
    {
        this.timeSpentSeconds = timeSpentSeconds;
    }


    public String getUpdateAuthor()
    {
        return updateAuthor;
    }


    public void setUpdateAuthor( final String updateAuthor )
    {
        this.updateAuthor = updateAuthor;
    }


    public Long getUpdateDate()
    {
        return updateDate;
    }


    public void setUpdateDate( final Long updateDate )
    {
        this.updateDate = updateDate;
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !( o instanceof IssueWorkLog ) )
        {
            return false;
        }

        final IssueWorkLog that = ( IssueWorkLog ) o;

        return !( workLogId != null ? !workLogId.equals( that.workLogId ) : that.workLogId != null );
    }


    @Override
    public int hashCode()
    {
        return workLogId != null ? workLogId.hashCode() : 0;
    }


    @Override
    public String toString()
    {
        return "IssueWorkLog{" +
                "workLogId='" + workLogId + '\'' +
                ", author='" + author + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
