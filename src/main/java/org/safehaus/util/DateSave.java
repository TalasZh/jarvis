package org.safehaus.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 * Created by kisik on 11.08.2015.
 */
public class DateSave
{
    private final Log log = LogFactory.getLog(DateSave.class);
    private static final String LAST_DATE_JIRA = "lastGatheredDateJira";
    private static final String LAST_DATE_STASH = "lastGatheredDateStash";
    private static final String LAST_DATE_SONAR = "lastGatheredDateSonar";
    private static final String LAST_DATE_CONFLUENCE = "lastGatheredDateConfluence";
    private static final String IS_SPARK_STARTED_ID = "IsSparkStartedAlready";

    Preferences prefs = Preferences.userNodeForPackage( DateSave.class );


    public void resetLastGatheredDateJira() throws BackingStoreException
    {
        prefs.putLong( LAST_DATE_JIRA, 0 );
        prefs.flush();
    }

    public void resetLastGatheredDateStash() throws BackingStoreException
    {
        prefs.putLong( LAST_DATE_STASH, 0 );
        prefs.flush();
    }

    public void resetLastGatheredDateSonar() throws BackingStoreException
    {
        prefs.putLong( LAST_DATE_SONAR, 0 );
        prefs.flush();
    }

    public void resetLastGatheredDateConfluence() throws BackingStoreException
    {
        prefs.putLong( LAST_DATE_CONFLUENCE, 0 );
        prefs.flush();
    }


    public void resetSparkStarted() throws BackingStoreException
    {
        prefs.putBoolean( IS_SPARK_STARTED_ID, false );
        prefs.flush();
    }

    public Date getLastGatheredDateJira() throws IOException
    {
        return new Date( prefs.getLong( LAST_DATE_JIRA, 0 ) );
    }


    public Date getLastGatheredDateStash() throws IOException
    {
        return new Date( prefs.getLong( LAST_DATE_STASH, 0 ) );
    }


    public Date getLastGatheredDateSonar() throws IOException
    {
        return new Date( prefs.getLong( LAST_DATE_SONAR, 0 ) );
    }


    public Date getLastGatheredDateConfluence() throws IOException
    {
        return new Date( prefs.getLong( LAST_DATE_CONFLUENCE, 0 ) );
    }

    public boolean getIsSparkStarted() throws IOException
    {
        return prefs.getBoolean(IS_SPARK_STARTED_ID, false);
    }

    public void saveIsSparkStarted(boolean sparkStarted)
    {
        prefs.putBoolean(IS_SPARK_STARTED_ID, sparkStarted);
    }

    public void saveLastGatheredDates( long lastGatheredDateJira, long lastGatheredDateStash,
                                       long lastGatheredDateSonar, long lastGatheredDateConfluence ) throws IOException
    {
        prefs.putLong( LAST_DATE_JIRA, lastGatheredDateJira );
        prefs.putLong( LAST_DATE_STASH, lastGatheredDateStash );
        prefs.putLong( LAST_DATE_SONAR, lastGatheredDateSonar );
        prefs.putLong( LAST_DATE_CONFLUENCE, lastGatheredDateConfluence );
        try
        {
            prefs.flush();
        }
        catch ( BackingStoreException e )
        {
            e.printStackTrace();
        }
    }
}
