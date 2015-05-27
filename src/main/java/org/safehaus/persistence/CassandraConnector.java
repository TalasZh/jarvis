package org.safehaus.persistence;

import com.datastax.driver.core.*;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

import java.util.Date;
import java.lang.StringBuilder;

/**
 * Created by neslihan on 26.05.2015.
 */
public class CassandraConnector {
    private Cluster cluster;
    private String  node;
    private Session session;
    private String keyspaceName = "jarvis";
    private String captureTableName = "capture";

    public enum ACTION{
        ADD, DROP
    }

    public CassandraConnector(String node)
    {
        this.node = new String(node);
        cluster = null;
    }

    public void connect()
    {
        try {
            cluster = Cluster.builder()
                    .addContactPoint(node).build();

            Metadata metadata = cluster.getMetadata();
            System.out.printf("Connected to cluster: %s\n",
                    metadata.getClusterName());
            for (Host host : metadata.getAllHosts()) {
                System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                        host.getDatacenter(), host.getAddress(), host.getRack());
            }

            session = cluster.connect();
        } catch(NoHostAvailableException e){
            System.out.println(e.getMessage());
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void close()
    {
        cluster.close();
    }

    public void persistCapture(int id, Date created, String annotationText,
                               String url, String ancestorId, String anchorText)
    {
        try {
            PreparedStatement insertCaptureStatement = session.prepare("INSERT INTO jarvis.capture " +
                    "(id, created, annotationText, url, ancestorId, anchorText)" +
                    " VALUES (?, ?, ?, ?, ?, ?);");
            BoundStatement boundStatement = new BoundStatement(insertCaptureStatement);
            session.execute(boundStatement.bind(id, created, annotationText, url, ancestorId, anchorText));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void createSchema(){

        try{
            // Create the keyspace if not yet exists
            StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ");
            sb.append(keyspaceName);
            sb.append(" WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 3}");

            session.execute(sb.toString());

            // Create capture table
            sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            sb.append(keyspaceName);
            sb.append(".");
            sb.append(captureTableName);
            sb.append(" (id int PRIMARY KEY, created timestamp, annotationText text, ");
            sb.append("url text, ancestorId text, anchorText text);");
            session.execute(sb.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet executeStatement(String query)
    {

        return session.execute(query);
    }

    public void addRemoveColumn(ACTION action, String keyspace, String tableName, String columnDetails)
    {
        StringBuilder sb = new StringBuilder();
        switch(action){
            case ADD:
                sb.append("ALTER TABLE " + keyspace + "." + tableName + " ADD " + columnDetails);
                session.execute(sb.toString());
                break;
            case DROP:
                sb.append("ALTER TABLE " + keyspace + "." + tableName +" DROP " + columnDetails);
                session.execute(sb.toString());
                break;
        }

    }
}
