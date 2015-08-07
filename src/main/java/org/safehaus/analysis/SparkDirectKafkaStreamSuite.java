package org.safehaus.analysis;

import akka.actor.Stash;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.safehaus.stash.model.StashMetricIssue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by neslihan on 07.08.2015.
 */
public class SparkDirectKafkaStreamSuite implements Serializable {
    private static String brokerList = new String("localhost:9092,localhost:9093");
    private static String sparkMaster = new String("local[3]");
    private static String appName = new String("JarvisStreamConsumer");

    public SparkDirectKafkaStreamSuite(){}

    public static void startStreams(){
        JavaStreamingContext jssc;
        SparkConf conf;
        HashSet<String> topicsSet;
        HashMap<String, String> kafkaParams;

        topicsSet = new HashSet<String>();

        kafkaParams = new HashMap<String, String>();
        kafkaParams.put("metadata.broker.list", brokerList);
        kafkaParams.put("auto.offset.reset","smallest");

        conf = new SparkConf().setAppName(appName);
        conf.setMaster(sparkMaster);
        jssc = new JavaStreamingContext(conf, Durations.seconds(60));

        //Create the stream for jira-logs
        topicsSet.add(JiraMetricIssueKafkaProducer.getTopic());
        JavaPairInputDStream<String, JiraMetricIssue> jiraLogDStream = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                JiraMetricIssue.class,
                StringDecoder.class,
                JiraMetricIssueKafkaSerializer.class,
                kafkaParams,
                topicsSet
        );

        jiraLogDStream.print();

        //Create the stream for stash-logs
        topicsSet.clear();
        topicsSet.add(StashMetricIssueKafkaProducer.getTopic());
        JavaPairInputDStream<String, StashMetricIssue> stashLogDStream = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                StashMetricIssue.class,
                StringDecoder.class,
                StashMetricIssueKafkaSerializer.class,
                kafkaParams,
                topicsSet
        );

        stashLogDStream.print();

        jssc.start();
        jssc.awaitTermination();
    }
}
