package org.safehaus.analysis;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.safehaus.confluence.model.ConfluenceMetric;

import java.util.Properties;

/**
 * Created by neslihan on 27.08.2015.
 */
public class ConfluenceMetricKafkaProducer {
    private Producer<String, ConfluenceMetric> producer;
    private static String topic = "confluencemetricissue";

    public ConfluenceMetricKafkaProducer()
    {
        Properties props = new Properties();
        props.put("zk.connect", "127.0.0.1:2181");
        props.put("serializer.class", "org.safehaus.analysis.ConfluenceMetricKafkaSerializer");
        props.put("metadata.broker.list", "localhost:9092");
        props.put("producer.type", "sync");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, ConfluenceMetric>(config);

    }

    public void send(ConfluenceMetric confluenceMetricIssue)
    {
        producer.send(new KeyedMessage<String, ConfluenceMetric>(topic, confluenceMetricIssue));
    }

    public void close()
    {
        producer.close();
    }

    public static String getTopic(){
        return topic;
    }
}
