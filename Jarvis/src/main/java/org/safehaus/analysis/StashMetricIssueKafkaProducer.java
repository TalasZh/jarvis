package org.safehaus.analysis;


import java.util.Properties;

import org.safehaus.dao.entities.stash.StashMetricIssue;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * Created by neslihan on 27.07.2015.
 */
public class StashMetricIssueKafkaProducer {
    private Producer<String, StashMetricIssue> producer;
    private static String topic = "stashmetricissue";

    public StashMetricIssueKafkaProducer()
    {
        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "org.safehaus.analysis.StashMetricIssueKafkaSerializer");

        props.put("metadata.broker.list", "localhost:9092");
        props.put("producer.type", "sync");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, StashMetricIssue>(config);
    }

    public void send(StashMetricIssue stashIssue)
    {
        producer.send(new KeyedMessage<String, StashMetricIssue>(topic, stashIssue));
    }

    public void close()
    {
        producer.close();
    }

    public static String getTopic(){
        return topic;
    }
}
