package org.safehaus.persistence;

import com.datastax.driver.core.*;

/**
 * Created by neslihan on 26.05.2015.
 */
public class CassandraConnector {
    private Cluster cluster;
    private String  node;
    private Session session;

    public CassandraConnector(String node){
        this.node = new String(node);
        cluster = null;
    }

    public void connect(){
        cluster = Cluster.builder()
                .addContactPoint(node).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n",
                metadata.getClusterName());
        for ( Host host : metadata.getAllHosts() ) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }

        session = cluster.connect();
    }

    public void close(){
        cluster.close();
    }

}
