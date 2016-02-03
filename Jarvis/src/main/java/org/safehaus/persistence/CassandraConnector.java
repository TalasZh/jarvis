package org.safehaus.persistence;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;


/**
 * Created by neslihan on 26.05.2015.
 */
public class CassandraConnector
{
    private Cluster cluster;
    private String node;
    private Session session;
    private static String keyspaceName = "jarvis";
    private static String captureTableName = "capture";
    private static CassandraConnector instance = null;
    private static final Log logger = LogFactory.getLog( CassandraConnector.class );


    private CassandraConnector()
    {
    }


    public synchronized static CassandraConnector getInstance()
    {
        if ( instance == null )
        {
            instance = new CassandraConnector();
            logger.info( "Created singleton instance for CassandraConnector" );
        }

        return instance;
    }


    public static String getKeyspace()
    {
        return keyspaceName;
    }


    public static String getCaptureTable()
    {
        return captureTableName;
    }


    public enum ACTION
    {
        ADD, DROP
    }


    public void connect( String node )
    {
        try
        {
            this.node = new String( node );
            cluster = Cluster.builder().addContactPoint( node ).build();

            Metadata metadata = cluster.getMetadata();
            System.out.printf( "Connected to cluster: %s\n", metadata.getClusterName() );
            for ( Host host : metadata.getAllHosts() )
            {
                System.out.printf( "Datacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(),
                        host.getRack() );
            }

            session = cluster.connect();
        }
        catch ( NoHostAvailableException e )
        {
            System.out.println( e.getMessage() );
        }
        catch ( Exception e )
        {
            System.out.println( e.getMessage() );
        }
    }


    public void close()
    {
        cluster.close();
    }


    public void persistCapture( int id, Date created, String annotationText, String url, String ancestorId,
                                String anchorText )
    {
        try
        {
            PreparedStatement insertCaptureStatement = session.prepare( "INSERT INTO jarvis.capture " +
                    "(id, created, annotationText, url, ancestorId, anchorText)" +
                    " VALUES (?, ?, ?, ?, ?, ?);" );
            BoundStatement boundStatement = new BoundStatement( insertCaptureStatement );
            session.execute( boundStatement.bind( id, created, annotationText, url, ancestorId, anchorText ) );
        }
        catch ( Exception e )
        {
            System.out.println( e.getMessage() );
        }
    }


    public void createSchema()
    {

        try
        {
            // Create the keyspace if not yet exists
            StringBuilder sb = new StringBuilder( "CREATE KEYSPACE IF NOT EXISTS " );
            sb.append( keyspaceName );
            sb.append( " WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 3}" );

            session.execute( sb.toString() );

            // Create capture table
            sb = new StringBuilder( "CREATE TABLE IF NOT EXISTS " );
            sb.append( keyspaceName );
            sb.append( "." );
            sb.append( captureTableName );
            sb.append( " (id int PRIMARY KEY, created timestamp, annotationText text, " );
            sb.append( "url text, ancestorId text, anchorText text);" );
            session.execute( sb.toString() );
        }
        catch ( Exception e )
        {
            System.out.println( e.getMessage() );
        }
    }


    public ResultSet executeStatement( String query )
    {
        try
        {
            return session.execute( query );
        }
        catch ( Exception ex )
        {
            logger.error("Query execution failed.", ex);
        }
        return null;
    }


    public void addRemoveColumn( ACTION action, String keyspace, String tableName, String columnDetails )
    {
        StringBuilder sb = new StringBuilder();
        switch ( action )
        {
            case ADD:
                sb.append( "ALTER TABLE " );
                sb.append( keyspace );
                sb.append( "." );
                sb.append( tableName );
                sb.append( " ADD " );
                sb.append( columnDetails );
                session.execute( sb.toString() );
                break;
            case DROP:
                sb.append( "ALTER TABLE " );
                sb.append( keyspace );
                sb.append( "." );
                sb.append( tableName );
                sb.append( " DROP " );
                sb.append( columnDetails );
                session.execute( sb.toString() );
                break;
        }
    }
}
