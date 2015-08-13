package org.safehaus.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created by kisik on 11.08.2015.
 */
public class DateSave {
    private static final String LAST_DATE_JIRA = "lastGatheredDateJira";
    private static final String LAST_DATE_STASH = "lastGatheredDateStash";
    private static final String LAST_DATE_SONAR = "lastGatheredDateSonar";
    private static final String LAST_DATE_CONFLUENCE = "lastGatheredDateConfluence";

    Preferences prefs = Preferences.userNodeForPackage(DateSave.class);

    public void resetLastGatheredDateJira() {
        prefs.putLong(LAST_DATE_JIRA, 0);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public Date getLastGatheredDateJira() throws IOException {
        return new Date(prefs.getLong(LAST_DATE_JIRA, 0));
    }

    public Date getLastGatheredDateStash() throws IOException {
        return new Date(prefs.getLong(LAST_DATE_STASH, 0));
    }

    public Date getLastGatheredDateSonar() throws IOException {
        return new Date(prefs.getLong(LAST_DATE_SONAR, 0));
    }

    public Date getLastGatheredDateConfluence() throws IOException {
        return new Date(prefs.getLong(LAST_DATE_CONFLUENCE, 0));
    }

    public void saveLastGatheredDates(long lastGatheredDateJira, long lastGatheredDateStash, long lastGatheredDateSonar, long lastGatheredDateConfluence) throws IOException {
        prefs.putLong(LAST_DATE_JIRA, lastGatheredDateJira);
        prefs.putLong(LAST_DATE_STASH, lastGatheredDateStash);
        prefs.putLong(LAST_DATE_SONAR, lastGatheredDateSonar);
        prefs.putLong(LAST_DATE_CONFLUENCE, lastGatheredDateConfluence);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

}
