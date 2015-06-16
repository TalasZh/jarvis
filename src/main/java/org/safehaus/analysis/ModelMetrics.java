package org.safehaus.analysis;


import java.io.Serializable;
import java.util.Date;

import org.safehaus.persistence.CassandraConnector;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.javaFunctions;
import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapRowTo;


/**
 * Created by neslihan on 12.06.2015.
 */
public class ModelMetrics
{
    private transient SparkConf conf;
    private transient JavaSparkContext jsc;
    private static String keyspace = CassandraConnector.getKeyspace();
    private static String table = CassandraConnector.getCaptureTable();


    public ModelMetrics( String sparkMaster, String appName, String cassandraHost )
    {
        conf = new SparkConf( true ).
                                            setMaster( sparkMaster ).
                                            setAppName( appName ).
                                            set( "spark.cassandra.connection.host", cassandraHost );
        jsc = new JavaSparkContext( conf );
    }


    public void getCaptureData()
    {
        JavaRDD<CaptureInternal> captureRDD =
                javaFunctions( jsc ).cassandraTable( keyspace, table, mapRowTo( CaptureInternal.class ) );
    }


    public void collectMetrics() {}


    public void saveMetrics() {}


    public class CaptureInternal implements Serializable
    {
        private Long id;
        private Date created = new Date();
        private String annotationtext;
        private String url;
        private String ancestorid;
        private String anchortext;
        private String newcol;


        public CaptureInternal() {}


        public CaptureInternal( Long id, Date created, String annotationtext, String url, String ancestorid,
                                String anchortext, String newcol )
        {
            this.id = id;
            this.created = created;
            this.annotationtext = annotationtext;
            this.url = url;
            this.ancestorid = ancestorid;
            this.anchortext = anchortext;
            this.newcol = newcol;
        }


        public Long getId()
        {
            return id;
        }


        public void setId()
        {
            this.id = id;
        }


        public Date getCreated()
        {
            return created;
        }


        public void setCreated( Date created )
        {
            this.created = created;
        }


        public String getAnnotationText()
        {
            return annotationtext;
        }


        public void setAnnotationText( String annotationText )
        {
            this.annotationtext = annotationText;
        }


        public String getUrl()
        {
            return url;
        }


        public void setUrl( String url )
        {
            this.url = url;
        }


        public String getAncestorId()
        {
            return this.ancestorid;
        }


        public void setAncestorId( String ancestorId )
        {
            this.ancestorid = ancestorId;
        }


        public String getAnchorText()
        {
            return this.anchortext;
        }


        public void setAnchorText( String anchorText )
        {
            this.anchortext = anchorText;
        }


        public String getNewcol()
        {
            return this.newcol;
        }


        public void setNewCol( String newcol )
        {
            this.newcol = newcol;
        }
    }
}
