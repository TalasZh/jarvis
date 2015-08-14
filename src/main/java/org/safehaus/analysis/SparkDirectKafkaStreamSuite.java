package org.safehaus.analysis;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.safehaus.stash.model.StashMetricIssue;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.common.base.Optional;

import kafka.serializer.StringDecoder;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;


/**
 * Created by neslihan on 07.08.2015.
 */
public class SparkDirectKafkaStreamSuite implements Serializable
{
    private static String brokerList = new String( "localhost:9092,localhost:9093" );
    private static String sparkMaster = new String( "local[3]" );
    private static String appName = new String( "JarvisStreamConsumer" );


    public SparkDirectKafkaStreamSuite()
    {
    }


    public static void startStreams()
    {
        JavaStreamingContext jssc;
        SparkConf conf;
        HashSet<String> topicsSet;
        HashMap<String, String> kafkaParams;

        topicsSet = new HashSet<String>();

        kafkaParams = new HashMap<String, String>();
        kafkaParams.put( "metadata.broker.list", brokerList );
        kafkaParams.put( "auto.offset.reset", "smallest" );

        conf = new SparkConf().setAppName( appName );
        conf.setMaster( sparkMaster );
        jssc = new JavaStreamingContext( conf, Durations.seconds( 60 ) );

        //Create the stream for jira-logs
        topicsSet.add( JiraMetricIssueKafkaProducer.getTopic() );
        JavaPairInputDStream<String, JiraMetricIssue> jiraLogDStream = KafkaUtils
                .createDirectStream( jssc, String.class, JiraMetricIssue.class, StringDecoder.class,
                        JiraMetricIssueKafkaSerializer.class, kafkaParams, topicsSet );

        jiraLogDStream.print();

        JavaDStream<JiraMetricIssue> jiraIssuesDStream =
                jiraLogDStream.map( new Function<Tuple2<String, JiraMetricIssue>, JiraMetricIssue>()
                {
                    public JiraMetricIssue call( Tuple2<String, JiraMetricIssue> tuple2 )
                    {
                        return tuple2._2();
                    }
                } );

        JavaPairDStream<String, JiraMetricIssue> jiraAuthorIssuesDStream =
                jiraIssuesDStream.mapToPair( new PairFunction<JiraMetricIssue, String, JiraMetricIssue>()
                {
                    public Tuple2<String, JiraMetricIssue> call( JiraMetricIssue jiraMetricIssue ) throws Exception
                    {
                        return new Tuple2<String, JiraMetricIssue>( jiraMetricIssue.getAssigneeName(),
                                jiraMetricIssue );
                    }
                } );

        JavaPairDStream<String, Integer> jiraAuthorIssueCountDStream =
                jiraAuthorIssuesDStream.mapToPair( new PairFunction<Tuple2<String, JiraMetricIssue>, String, Integer>()
                {
                    public Tuple2<String, Integer> call( Tuple2<String, JiraMetricIssue> JiraMetricIssueTuple2 )
                            throws Exception
                    {
                        return new Tuple2<String, Integer>( JiraMetricIssueTuple2._1(), 1 );
                    }
                } ).reduceByKey( new Function2<Integer, Integer, Integer>()
                {
                    public Integer call( Integer i1, Integer i2 ) throws Exception
                    {
                        return i1 + i2;
                    }
                } );

        jiraAuthorIssueCountDStream.print();

        JavaPairDStream<String, JiraMetricIssue> jiraFinishedFilteredIssuesDStream =
                jiraAuthorIssuesDStream.filter( new Function<Tuple2<String, JiraMetricIssue>, Boolean>()
                {
                    public Boolean call( Tuple2<String, JiraMetricIssue> jiraMetricIssueTuple2 ) throws Exception
                    {
                        return ( jiraMetricIssueTuple2._2().getUpdateDate() != null
                                && jiraMetricIssueTuple2._2().getStatus() != null );
                    }
                } );

        final Function2<List<Tuple4<Integer, Integer, Integer, Integer>>, Optional<Tuple4<Integer, Integer, Integer,
                Integer>>, Optional<Tuple4<Integer, Integer, Integer, Integer>>>
                updateProductivityMetricAndSaveDb =
                new Function2<List<Tuple4<Integer, Integer, Integer, Integer>>, Optional<Tuple4<Integer, Integer,
                        Integer, Integer>>, Optional<Tuple4<Integer, Integer, Integer, Integer>>>()
                {
                    Integer mostRecentYear = null;
                    Integer mostRecentMonth = null;


                    @Override
                    public Optional<Tuple4<Integer, Integer, Integer, Integer>> call(
                            List<Tuple4<Integer, Integer, Integer, Integer>> values,
                            Optional<Tuple4<Integer, Integer, Integer, Integer>> state )
                    {
                        Tuple4<Integer, Integer, Integer, Integer> newSum =
                                state.or( new Tuple4<Integer, Integer, Integer, Integer>( 0, 0, 0, 0 ) );
                        Integer finishedCount = newSum._1();
                        Integer allCount = newSum._2();
                        Integer thisMonth = -1;
                        Integer thisYear = -1;
                        boolean initialized = false;
                        for ( Tuple4<Integer, Integer, Integer, Integer> value : values )
                        {
                            finishedCount += value._1();
                            allCount += value._2();

                            if ( !initialized )
                            {
                                thisMonth = value._3();
                                thisYear = value._4();
                                if ( mostRecentYear == null || thisYear > mostRecentYear || thisYear == mostRecentYear )
                                {
                                    if ( mostRecentYear == thisYear )
                                    {
                                        if ( mostRecentMonth < thisMonth )
                                        {
                                            mostRecentMonth = thisMonth;
                                        }
                                    }
                                    else
                                    {
                                        mostRecentYear = thisYear;
                                        mostRecentMonth = thisMonth;
                                    }
                                }

                                initialized = true;
                            }
                        }

                        if ( !initialized )
                        {
                            return Optional.of( state.get() );
                        }
                        else
                        {
                            newSum = new Tuple4<Integer, Integer, Integer, Integer>( finishedCount, allCount, thisMonth,
                                    thisYear );
                            return Optional.of( newSum );
                        }
                    }
                };

        // author-month-year,finishedCnt-totalCnt-jiraMetricIssue
        JavaPairDStream<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer, Integer>>
                jiraUserProductivityMetric = jiraFinishedFilteredIssuesDStream.mapToPair(
                new PairFunction<Tuple2<String, JiraMetricIssue>, Tuple3<String, Integer, Integer>, Tuple4<Integer,
                        Integer, Integer, Integer>>()
                {
                    public Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer, Integer>> call(
                            Tuple2<String, JiraMetricIssue> stringJiraMetricIssueTuple2 ) throws Exception
                    {
                        Integer month, year;
                        Date updateDt = stringJiraMetricIssueTuple2._2().getUpdateDate();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime( updateDt );

                        month = cal.get( Calendar.MONTH );
                        year = cal.get( Calendar.YEAR ) + 1; // zero-based

                        if ( stringJiraMetricIssueTuple2._2().getStatus().compareToIgnoreCase( "Closed" ) == 0
                                || stringJiraMetricIssueTuple2._2().getStatus().compareToIgnoreCase( "Resolved" ) == 0 )
                        {
                            System.out.println( "THIS IS WORKING" );
                            return new Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,
                                    Integer>>(
                                    new Tuple3<String, Integer, Integer>( stringJiraMetricIssueTuple2._1(), month,
                                            year ),
                                    new Tuple4<Integer, Integer, Integer, Integer>( 1, 1, month, year ) );
                        }
                        else
                        {
                            System.out.println( "THIS IS WORKING TOO" );

                            return new Tuple2<Tuple3<String, Integer, Integer>, Tuple4<Integer, Integer, Integer,
                                    Integer>>(
                                    new Tuple3<String, Integer, Integer>( stringJiraMetricIssueTuple2._1(), month,
                                            year ),
                                    new Tuple4<Integer, Integer, Integer, Integer>( 0, 1, month, year ) );
                        }
                    }
                } ).updateStateByKey( updateProductivityMetricAndSaveDb );

        jiraUserProductivityMetric.print();

        //Create the stream for stash-logs
        topicsSet.clear();
        topicsSet.add( StashMetricIssueKafkaProducer.getTopic() );
        JavaPairInputDStream<String, StashMetricIssue> stashLogDStream = KafkaUtils
                .createDirectStream( jssc, String.class, StashMetricIssue.class, StringDecoder.class,
                        StashMetricIssueKafkaSerializer.class, kafkaParams, topicsSet );

        stashLogDStream.print();

        jssc.checkpoint( "/tmp/spark_checkpoint_dir/" );
        jssc.start();
        jssc.awaitTermination();
    }
}
