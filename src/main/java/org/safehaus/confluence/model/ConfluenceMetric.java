package org.safehaus.confluence.model;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by kisik on 10.08.2015.
 */
public class ConfluenceMetric implements Serializable
{
    private int pageID;
    private String authorUsername;
    private String authorDisplayName;
    private String authorUserKey;
    private Date when;
    private int versionNumber;
    private String title;
    private long bodyLength;

    public ConfluenceMetric() {}

    public int getPageID() {
        return pageID;
    }

    public void setPageID(int pageID) {
        this.pageID = pageID;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public String getAuthorUserKey() {
        return authorUserKey;
    }

    public void setAuthorUserKey(String authorUserKey) {
        this.authorUserKey = authorUserKey;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(long bodyLength) {
        this.bodyLength = bodyLength;
    }
}
